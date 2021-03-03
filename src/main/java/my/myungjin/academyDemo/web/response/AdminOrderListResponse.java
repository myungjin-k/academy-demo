package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayOption;
import my.myungjin.academyDemo.domain.order.DeliveryItem;
import my.myungjin.academyDemo.domain.order.OrderItem;

import java.util.Optional;

@Getter
public class AdminOrderListResponse {

    private String orderId;

    private String orderItemId;

    private String itemName;

    private String size;

    private String color;

    private int count;

    private String deliveryStatus;

    public AdminOrderListResponse of(OrderItem entity){
        this.orderId = entity.getOrder().getId();
        this.orderItemId = entity.getId();
        ItemDisplay display = entity.getItemOption().getItemDisplay();
        this.itemName = display.getItemDisplayName();
        ItemDisplayOption option = entity.getItemOption();
        this.size = option.getSize();
        this.color = option.getColor();
        this.count = entity.getCount();
        Optional<DeliveryItem> deliveryItem = Optional.ofNullable(entity.getDeliveryItem());
        this.deliveryStatus = deliveryItem.map(d -> d.getDelivery().getStatus().getDescription()).orElse("");
        return this;
    }

}
