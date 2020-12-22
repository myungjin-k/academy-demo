package my.myungjin.academyDemo.service.order;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import my.myungjin.academyDemo.domain.order.*;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final DeliveryRepository deliveryRepository;

    private final CartRepository cartRepository;

    private final MemberRepository memberRepository;


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

    private Order saveOrderItems(List<Id<CartItem, String>> itemIds, Order order){
        List<CartItem> items = itemIds.stream()
                .map(itemId ->
                        cartRepository.findById(itemId.value())
                                .orElseThrow(() -> new NotFoundException(CartItem.class, itemId)))
                .collect(Collectors.toList());

        int totalAmount = 0;
        for(CartItem item : items){
            OrderItem oItem = new OrderItem(Util.getUUID());
            oItem.setItemOption(item.getItemOption());
            save(oItem);
            order.addItem(oItem);
            totalAmount += oItem.getItemOption().getItemDisplay().getSalePrice() * item.getCount();
        }
        order.setTotalAmount(totalAmount);
        StringBuilder abbrOrderItems = new StringBuilder();
        abbrOrderItems.append( items.get(0).getItemOption().getItemDisplay().getItemDisplayName());
        if(items.size() > 1)
            abbrOrderItems.append("外 ").append(items.size() - 1).append("건");
        order.setAbbrOrderItems(abbrOrderItems.toString());
        deleteCartItems(items);
        return order;
    }

    private Order saveDelivery(Delivery delivery, Order order){
        deliveryRepository.save(delivery);
        order.addDelivery(delivery);
        return order;
    }
    private void save(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }

    private Order findById(Id<Order, String> orderId){
        return orderRepository.findById(orderId.value())
                .orElseThrow(() -> new NotFoundException(Order.class, orderId));
    }

    private Order save(Order order){
        return orderRepository.save(order);
    }
    private void deleteCartItems(List<CartItem> items){
        cartRepository.deleteAll(items);
    }
}
