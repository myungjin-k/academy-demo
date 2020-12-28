package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.domain.order.OrderItem;
import my.myungjin.academyDemo.util.Util;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
public class DeliveryRequest {

    private String receiverName;

    private String receiverTel;

    private String receiverAddr1;

    private String receiverAddr2;

    private int status;

    private String message;

    private List<OrderItem> orderItems;

    public Delivery newDelivery(){
        return Delivery.builder()
                .id(Util.getUUID())
                .receiverName(receiverName)
                .receiverTel(receiverTel)
                .receiverAddr1(receiverAddr1)
                .receiverAddr2(receiverAddr2)
                .status(DeliveryStatus.of(status))
                .build();
    }
}
