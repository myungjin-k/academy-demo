package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class DeliveryItemRequest {

    private String deliveryId;

    private String itemId;

    private int count;

}
