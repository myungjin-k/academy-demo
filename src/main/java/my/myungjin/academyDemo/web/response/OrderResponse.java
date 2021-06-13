package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.Order;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class OrderResponse {

    private String orderId;

    private String orderDate;

    private String status;

    private String abbrItemName;

    private int totalAmount;

    private int payAmount;

    private List<OrderItemResponse> items = new ArrayList<>();

    public OrderResponse of(Order entity){
        this.orderId = entity.getId();
        this.orderDate = entity.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        this.abbrItemName = entity.getAbbrOrderItems();
        this.totalAmount = entity.getTotalAmount();
        this.payAmount = this.totalAmount - entity.getUsedPoints() - entity.getCouponDiscounted() - entity.getItemDiscounted();
        Delivery latestDelivery = entity.getLatestDelivery();
        this.status = latestDelivery.getStatus().getDescription();
        this.items = entity.getItems().stream()
                .map(orderItem -> new OrderItemResponse().of(orderItem))
                .collect(Collectors.toList());
        return this;
    }

}
