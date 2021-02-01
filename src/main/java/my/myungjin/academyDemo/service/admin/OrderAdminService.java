package my.myungjin.academyDemo.service.admin;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayOptionRepository;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.*;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.error.StatusNotSatisfiedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderAdminService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryItemRepository deliveryItemRepository;

    private final ItemDisplayOptionRepository itemDisplayOptionRepository;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    @Transactional(readOnly = true)
    public Page<OrderItem> searchOrders(Id<Order, String> orderId, LocalDate start, LocalDate end, Pageable pageable){
        return orderItemRepository.findAll(OrderItemPredicate.search(orderId, start, end), pageable);
    }

    @Transactional(readOnly = true)
    public Order getOrderDetail(@Valid Id<Order, String> orderId) {
        Order o = orderRepository.findById(orderId.value())
                .orElseThrow(() -> new NotFoundException(Order.class, orderId));
        o.setItems(orderItemRepository.findAllByOrder(o));
        o.setDeliveries(deliveryRepository.getAllByOrderOrderByCreateAtDesc(o));
        return o;
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findAllOrderItems(@Valid Id<Delivery, String> deliveryId){
        return deliveryRepository.findById(deliveryId.value())
                .map(delivery -> {
                    Order order = delivery.getOrder();
                    order.setDeliveries(deliveryRepository.getAllByOrderOrderByCreateAtDesc(order));
                    return orderItemRepository.findAllByOrder(order);
                }).orElseThrow(() -> new NotFoundException(Delivery.class, deliveryId));
    }

    @Transactional(readOnly = true)
    public Delivery findById(@Valid Id<Delivery, String> deliveryId){
        return deliveryRepository.findById(deliveryId.value())
                .map(delivery -> {
                    delivery.setItems(deliveryItemRepository.findAllByDelivery(delivery));
                    return delivery;
                }).orElseThrow(() -> new NotFoundException(Delivery.class, deliveryId));
    }

    public Optional<DeliveryItem> findItem(@Valid Id<Delivery, String> deliveryId, @Valid Id<DeliveryItem, String> itemId){
        return deliveryItemRepository.findByDeliveryIdAndId(deliveryId.value(), itemId.value());
    }

    @Transactional
    public Delivery addDelivery(@Valid Id<Order, String> orderId, @Valid Delivery delivery, List<Id<OrderItem, String>> itemIds){
        Order order = orderRepository.findById(orderId.value())
                .orElseThrow(() -> new NotFoundException(Order.class, orderId));
        delivery.setOrder(order);
        Delivery d = save(delivery);

        for(Id<OrderItem, String> id : itemIds){
            OrderItem item = orderItemRepository.getOne(id.value());
            String oId = item.getOrder().getId();
            if(!oId.equals(orderId.value()))
                throw new IllegalArgumentException("bad order id (item order id=" + oId + ", order id=" + orderId.value() + ")");
            DeliveryItem deliveryItem = new DeliveryItem(item.getCount());
            d.addItem(deliveryItem);
            deliveryItem.setItemOption(item.getItemOption());
            save(deliveryItem);
        }
        return d;
    }

    @Transactional
    public Delivery deleteDelivery(@Valid Id<Delivery, String> deliveryId){
        return modifyStatus(deliveryId, DeliveryStatus.DELETED);
    }

    @Transactional
    public Optional<DeliveryItem> addDeliveryItem(@Valid Id<Delivery, String> deliveryId,
                                                  @Valid Id<ItemDisplay.ItemDisplayOption, String> itemId, int count){
        Delivery delivery = deliveryRepository.findById(deliveryId.value())
                .orElseThrow(() -> new NotFoundException(Delivery.class, deliveryId));
        if(deliveryItemRepository.existsByDeliveryIdAndItemOptionId(deliveryId.value(), itemId.value()))
            return Optional.empty();
        DeliveryItem item = new DeliveryItem(count);
        delivery.addItem(item);
        saveItem(item, itemId);
        return Optional.of(item);
    }

    @Transactional
    public DeliveryItem deleteDeliveryItem(@Valid Id<Delivery, String> deliveryId, @Valid Id<DeliveryItem, String> itemId){
        return findItem(deliveryId, itemId)
                .map(deliveryItem -> {
                    if(!deliveryItem.getDelivery().getStatus().equals(DeliveryStatus.PROCESSING)){
                        throw new StatusNotSatisfiedException(DeliveryItem.class, Id.of(DeliveryItem.class, deliveryItem.getId()), deliveryId, itemId);
                    }
                    delete(deliveryItem);
                    return deliveryItem;
                }).orElseThrow(() -> new NotFoundException(DeliveryItem.class, deliveryId, itemId));
    }

    @Transactional
    public DeliveryItem modifyDeliveryItemCount(@Valid Id<Delivery, String> deliveryId,
                                                @Valid Id<DeliveryItem, String> itemId, int count){
        return findItem(deliveryId, itemId)
                .map(deliveryItem -> {
                    if(!deliveryItem.getDelivery().getStatus().equals(DeliveryStatus.PROCESSING)){
                        throw new StatusNotSatisfiedException(DeliveryItem.class, Id.of(DeliveryItem.class, deliveryItem.getId()), deliveryId, itemId);
                    }
                    deliveryItem.modifyCount(count);
                    return save(deliveryItem);
                }).orElseThrow(() -> new NotFoundException(DeliveryItem.class, deliveryId, itemId));
    }

    @Transactional
    public Delivery updateInvoice(@Valid Id<Delivery, String> deliveryId, @NotBlank String invoiceNum){
        Delivery d = findById(deliveryId);
        d.updateInvoice(invoiceNum);
        return save(d);
    }

    @Transactional
    public Delivery modifyStatus(@Valid Id<Delivery, String> deliveryId, DeliveryStatus status){
        Delivery d = findById(deliveryId);
        d.updateStatus(status);
        return save(d);
    }

    @Transactional
    public Delivery modifyAddress(@Valid Id<Delivery, String> deliveryId, @NotBlank String addr1, @NotBlank String addr2) {
        Delivery d = findById(deliveryId);
        d.updateAddress(addr1, addr2);
        return save(d);
    }
    private void saveItem(DeliveryItem deliveryItem, Id<ItemDisplay.ItemDisplayOption, String> itemId){
        itemDisplayOptionRepository.findById(itemId.value())
            .map(displayOption -> {
                deliveryItem.setItemOption(displayOption);
                return save(deliveryItem);
            }).orElseThrow(() ->  new NotFoundException(ItemDisplay.ItemDisplayOption.class, itemId));
    }

    private DeliveryItem save(DeliveryItem item){
        return deliveryItemRepository.save(item);
    }

    private Delivery save(Delivery delivery){
        return deliveryRepository.save(delivery);
    }

    private void delete(DeliveryItem deliveryItem){
        deliveryItemRepository.delete(deliveryItem);
    }
}