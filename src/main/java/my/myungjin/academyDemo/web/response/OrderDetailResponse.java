package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.order.Delivery;
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

    private int totalAmount;

    private int usedPoints;

    private int payAmount;

    private List<OrderItemResponse> items = new ArrayList<>();

    private String deliveryName;

    private String deliveryAddr;

    private String deliveryTel;

    private String deliveryMessage;

    public OrderDetailResponse of(Order entity){
        this.orderId = entity.getId();
        this.orderName = entity.getOrderName();
        this.orderDate = entity.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        this.totalAmount = entity.getTotalAmount();
        this.usedPoints = entity.getUsedPoints();
        this.payAmount = this.totalAmount - this.usedPoints;
        this.items = entity.getItems().stream()
                .map(orderItem -> new OrderItemResponse().of(orderItem))
                .collect(Collectors.toList());
        Delivery d = entity.getDeliveries().get(0);
        this.orderStatus = d.getStatus().getDescription();
        this.deliveryName = d.getReceiverName();
        this.deliveryAddr = StringUtils.join(d.getReceiverAddr1(), d.getReceiverAddr2(), " ");
        this.deliveryTel = d.getReceiverTel();
        this.deliveryMessage = d.getMessage();
        return this;
    }

}
