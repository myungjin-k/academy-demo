package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.order.DeliveryItem;
import my.myungjin.academyDemo.domain.order.OrderItem;

import java.util.Optional;

@Getter
public class AdminDeliveryListResponse {

    private String orderId;

    private String deliveryItemId;

    private String itemName;

    private String size;

    private String color;

    private int count;

    private String deliveryStatus;

    private String deliveryId;

    public AdminDeliveryListResponse of(DeliveryItem entity){
        this.orderId = entity.getOrderItem().getOrder().getId();
        this.deliveryItemId = entity.getId();
        ItemDisplay display = entity.getItemOption().getItemDisplay();
        this.itemName = display.getItemDisplayName();
        ItemDisplay.ItemDisplayOption option = entity.getItemOption();
        this.size = option.getSize();
        this.color = option.getColor();
        this.count = entity.getCount();
        this.deliveryStatus = entity.getDelivery().getStatus().getDescription();
        this.deliveryId = entity.getDelivery().getId();
        return this;
    }

}
