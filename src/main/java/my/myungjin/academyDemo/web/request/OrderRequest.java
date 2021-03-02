package my.myungjin.academyDemo.web.request;

import lombok.*;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.order.CartItem;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.domain.order.Order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor
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

    private String paymentUid;

    @Builder
    public OrderRequest(String name, String email, String tel, String addr1, String addr2, int usedPoints,
                        String receiverName, String receiverTel, String receiverAddr1, String receiverAddr2,
                        int status, String message, List<String> cartItemIds, List<CartRequest> items, String paymentUid) {
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.usedPoints = usedPoints;
        this.receiverName = receiverName;
        this.receiverTel = receiverTel;
        this.receiverAddr1 = receiverAddr1;
        this.receiverAddr2 = receiverAddr2;
        this.status = status;
        this.message = message;
        this.cartItemIds = cartItemIds;
        this.items = items;
        this.paymentUid = paymentUid;
    }

    public Order newOrder(){
        return Order.builder()
                .orderName(name)
                .orderEmail(email)
                .orderTel(tel)
                .orderAddr1(addr1)
                .orderAddr2(addr2)
                .usedPoints(usedPoints)
                .paymentUid(paymentUid)
                .build();
    }

    public Delivery newDelivery(){
        return Delivery.builder()
                .receiverName(receiverName)
                .receiverTel(receiverTel)
                .receiverAddr1(receiverAddr1)
                .receiverAddr2(receiverAddr2)
                .message(message)
                .status(DeliveryStatus.REQUESTED)
                .build();
    }

    public Order toOrder(Id<Order, String> orderId){
        return Order.builder()
                .id(orderId.value())
                .orderName(name)
                .orderTel(tel)
                .orderAddr1(addr1)
                .orderAddr2(addr2)
                .orderEmail(email)
                .paymentUid(paymentUid)
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

    public void addItem(CartRequest cartItem){
        items.add(cartItem);
    }
}
