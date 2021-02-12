package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.order.CartItem;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.domain.order.Order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@ToString
@Getter
public class OrderRequest {

    private String name;

    private String email;

    private String tel;

    private String addr1;

    private String addr2;

    private int usedPoints;

    private String receiverName;

    private String receiverTel;

    private String receiverAddr1;

    private String receiverAddr2;

    private int status;

    private String message;

    // 일반 주문
    private List<String> cartItemIds;

    // 수기 주문
    private List<CartRequest> items;

    public Order newOrder(){
        return Order.builder()
                .orderName(name)
                .orderEmail(email)
                .orderTel(tel)
                .orderAddr1(addr1)
                .orderAddr2(addr2)
                .usedPoints(usedPoints)
                .build();
    }

    public Delivery newDelivery(){
        return Delivery.builder()
                .receiverName(receiverName)
                .receiverTel(receiverTel)
                .receiverAddr1(receiverAddr1)
                .receiverAddr2(receiverAddr2)
                .message(message)
                .status(DeliveryStatus.PROCESSING)
                .build();
    }

    public Order toOrder(Id<Order, String> orderId){
        return Order.builder()
                .id(orderId.value())
                .orderName(name)
                .orderTel(tel)
                .orderAddr1(addr1)
                .orderAddr2(addr2)
                .build();
    }

    public Delivery toDelivery(Id<Delivery, String> deliveryId){
        return Delivery.builder()
                .id(deliveryId.value())
                .receiverName(receiverName)
                .receiverTel(receiverTel)
                .receiverAddr1(receiverAddr1)
                .receiverAddr2(receiverAddr2)
                .message(message)
                .status(DeliveryStatus.of(status))
                .build();
    }

    public List<Id<CartItem, String>> collectItems(){
        return cartItemIds.stream()
                .map(s -> Id.of(CartItem.class, s))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> collectItemsAndCounts(){
        return items.stream()
                .collect(Collectors.toMap(CartRequest::getItemId, CartRequest::getCount));
    }
}
