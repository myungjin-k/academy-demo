package my.myungjin.academyDemo.service.order;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayOptionRepository;
import my.myungjin.academyDemo.domain.order.*;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.error.StatusNotSatisfiedException;
import my.myungjin.academyDemo.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryItemRepository deliveryItemRepository;

    private final ItemDisplayOptionRepository itemDisplayOptionRepository;

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Delivery findById(@Valid Id<Delivery, String> deliveryId){
        return deliveryRepository.findById(deliveryId.value())
                .map(delivery -> {
                    delivery.setItems(deliveryItemRepository.findAllByDelivery(delivery));
                    return delivery;
                }).orElseThrow(() -> new NotFoundException(Delivery.class, deliveryId));
    }

    @Transactional(readOnly = true)
    public Optional<DeliveryItem> findItemByDeliveryAndItem(@Valid Id<Delivery, String> deliveryId, @Valid Id<ItemDisplay.ItemDisplayOption, String> itemId){
        return deliveryItemRepository.findByDelivery_idAndItemOption_id(deliveryId.value(), itemId.value());
    }

    @Transactional
    public Optional<DeliveryItem> addDeliveryItem(@Valid Id<Delivery, String> deliveryId, @Valid Id<ItemDisplay.ItemDisplayOption, String> itemId){
        return deliveryRepository.findById(deliveryId.value())
                .map(delivery -> {
                    boolean chk = deliveryItemRepository.existsByDelivery_idAndItemOption_id(deliveryId.value(), itemId.value());
                    if(chk)
                        return Optional.<DeliveryItem>empty();
                    DeliveryItem item = new DeliveryItem(Util.getUUID());
                    delivery.addItem(item);
                    saveItem(item, itemId);
                    return Optional.of(item);
                }).orElseThrow(() -> new NotFoundException(Delivery.class, deliveryId));
    }

    @Transactional
    public DeliveryItem deleteDeliveryItem(@Valid Id<Delivery, String> deliveryId, @Valid Id<ItemDisplay.ItemDisplayOption, String> itemId){
        return findItemByDeliveryAndItem(deliveryId, itemId)
                .map(deliveryItem -> {
                    if(!deliveryItem.getDelivery().getStatus().equals(DeliveryStatus.PROCESSING)){
                        throw new StatusNotSatisfiedException(DeliveryItem.class, Id.of(DeliveryItem.class, deliveryItem.getId()), deliveryId, itemId);
                    }
                    delete(deliveryItem);
                    return deliveryItem;
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
