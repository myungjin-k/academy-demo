package my.myungjin.academyDemo.service.order;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import my.myungjin.academyDemo.domain.order.*;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.domain.review.ReviewRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.error.StatusNotSatisfiedException;
import my.myungjin.academyDemo.util.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final DeliveryRepository deliveryRepository;

    private final CartRepository cartRepository;

    private final MemberRepository memberRepository;

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Page<Order> findAllMyMemberWithPage(@Valid Id<Member, String> memberId, Pageable pageable){
        return orderRepository.findAllByMember_id(memberId.value(), pageable);
    }

    @Transactional(readOnly = true)
    public Order findById(@Valid Id<Member, String> memberId, @Valid Id<Order, String> orderId){
        Order o = orderRepository.findByMember_idAndId(memberId.value(), orderId.value())
                .map(order -> {
                    order.setItems(orderItemRepository.findAllByOrder(order));
                    order.setDeliveries(deliveryRepository.findByOrder(order));
                    return order;
                }).orElseThrow(() -> new NotFoundException(Order.class, memberId, orderId));
        for(OrderItem item : o.getItems()){
            String reviewId = findReviewByItemAndMember(
                        Id.of(ItemDisplay.class, item.getItemOption().getItemDisplay().getId()),
                        memberId
                    ).map(Review::getId)
                    .orElse("");
            item.setReviewId(reviewId);
        }
        return o;
    }


    private Optional<Review> findReviewByItemAndMember(Id<ItemDisplay, String> itemId, Id<Member, String> memberId){

        return reviewRepository.findByItem_idAndMember_id(itemId.value(), memberId.value());
    }
    @Transactional
    public Order ordering(@Valid Id<Member, String> memberId, @Valid Order newOrder,
                          @Valid Delivery delivery, List<Id<CartItem, String>> itemIds){
        return memberRepository.findById(memberId.value())
                .map(member -> {
                    newOrder.setMember(member);
                    return save(newOrder);
                })
                .map(order -> saveOrderItems(itemIds, newOrder))
                .map(order -> saveDelivery(delivery, order))
                .orElseThrow(() ->  new NotFoundException(Member.class, memberId));
    }

    @Transactional
    public Order modify(@Valid Id<Member, String> memberId, @Valid Id<Order, String> orderId, @Valid Order order) {
        Order o = findById(memberId, orderId);
        Delivery d = deliveryRepository.findByOrder_Member_idAndOrder_id(memberId.value(), orderId.value()).get(0);
        if(d.getStatus().getValue() > 1){
            throw new StatusNotSatisfiedException(Order.class, orderId, d.getStatus());
        }
        o.modify(order.getOrderName(), order.getOrderTel(), order.getOrderAddr1(), order.getOrderAddr2());
        return save(o);
    }

    @Transactional
    public Delivery modify(@Valid Id<Member, String> memberId, @Valid Id<Order, String> orderId,
                           @Valid Id<Delivery, String> deliveryId, @Valid Delivery delivery) {
        Delivery d = deliveryRepository.findByOrder_Member_idAndOrder_idAndId(memberId.value(), orderId.value(), deliveryId.value())
                .orElseThrow(() -> new NotFoundException(Delivery.class, memberId, orderId, deliveryId));
        if(d.getStatus().getValue() > 1){
            throw new StatusNotSatisfiedException(Delivery.class, deliveryId, d.getStatus());
        }
        d.modify(delivery.getReceiverName(), delivery.getReceiverTel(),
                delivery.getReceiverAddr1(), delivery.getReceiverAddr2(), delivery.getMessage());
        return save(d);
    }

    @Transactional
    public Delivery modifyStatus(@Valid Id<Delivery, String> deliveryId, DeliveryStatus status){
        return deliveryRepository.findById(deliveryId.value())
                .map(delivery -> {
                    delivery.updateStatus(status);
                    return save(delivery);
                }).orElseThrow(() -> new NotFoundException(Delivery.class, deliveryId));
    }

    private Order saveOrderItems(List<Id<CartItem, String>> itemIds, Order order){
        List<CartItem> items = itemIds.stream()
                .map(itemId ->
                        cartRepository.findById(itemId.value())
                                .orElseThrow(() -> new NotFoundException(CartItem.class, itemId)))
                .collect(Collectors.toList());

        int totalAmount = 0;
        for(CartItem item : items){
            OrderItem oItem = new OrderItem(Util.getUUID(), item.getCount());
            oItem.setItemOption(item.getItemOption());
            order.addItem(oItem);
            save(oItem);
            totalAmount += oItem.getItemOption().getItemDisplay().getSalePrice() * item.getCount();
        }
        order.setTotalAmount(totalAmount);
        StringBuilder abbrOrderItems = new StringBuilder();
        abbrOrderItems.append( items.get(0).getItemOption().getItemDisplay().getItemDisplayName());
        if(items.size() > 1)
            abbrOrderItems.append("外 ").append(items.size() - 1).append("건");
        order.setAbbrOrderItems(abbrOrderItems.toString());
        deleteCartItems(items);
        return save(order);
    }

    private Order saveDelivery(Delivery delivery, Order order){
        order.addDelivery(delivery);
        save(delivery);
        return order;
    }

    private Delivery save(Delivery delivery){
        return deliveryRepository.save(delivery);
    }

    private void save(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }

    private Order save(Order order){
        return orderRepository.save(order);
    }

    private void deleteCartItems(List<CartItem> items){
        cartRepository.deleteAll(items);
    }
}
