package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.domain.order.Order;
import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDetailResponse {

    private String orderId;

    private String orderDate;

    private String orderName;

    private String orderStatus;

    private int orderAmount;

    private int payAmount;

    private int discountedAmount;

    private int usedPoints;

    private List<OrderItemResponse> items = new ArrayList<>();

    private String deliveryId;

    private String deliveryName;

    private String deliveryAddr1;

    private String deliveryAddr2;

    private String deliveryTel;

    private String deliveryMessage;

    public OrderDetailResponse of(Order entity){
        this.orderId = entity.getId();
        this.orderName = entity.getOrderName();
        this.orderDate = entity.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        this.items = entity.getItems().stream()
                .map(orderItem -> new OrderItemResponse().of(orderItem))
                .collect(Collectors.toList());
        this.orderAmount = entity.getTotalAmount();
        int discountAmount = 0;
        for(OrderItemResponse item : items){
            discountAmount += (item.getItemPrice() - item.getSalePrice()) * item.getCount();
        }
        this.discountedAmount = discountAmount;
        this.usedPoints = entity.getUsedPoints();
        this.payAmount = this.orderAmount - this.discountedAmount - this.usedPoints;
        Delivery d = entity.getDeliveries()
                .stream()
                .filter(delivery -> !delivery.getStatus().equals(DeliveryStatus.DELETED))
                .collect(Collectors.toList())
                .get(0);
        this.orderStatus = d.getStatus().getDescription();
        this.deliveryId = d.getId();
        this.deliveryName = d.getReceiverName();
        this.deliveryAddr1 = d.getReceiverAddr1();
        this.deliveryAddr2 = d.getReceiverAddr2();
        this.deliveryTel = d.getReceiverTel();
        this.deliveryMessage = d.getMessage();
        return this;
    }

}
