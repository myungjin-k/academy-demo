package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.order.DeliveryItem;
import my.myungjin.academyDemo.domain.order.OrderItem;

import java.util.Optional;

@Getter
public class OrderItemResponse {

    private String orderItemId;

    private String itemDisplayId;

    private String itemName;

    private int itemPrice;

    private String thumbnail;

    private String itemOptionId;

    private String size;

    private String color;

    private int count;

    private String deliveryStatus;

    private String invoiceNum;

    public OrderItemResponse of(OrderItem entity){
        this.orderItemId = entity.getId();
        ItemDisplay display = entity.getItemOption().getItemDisplay();
        this.itemDisplayId = display.getId();
        this.itemName = display.getItemDisplayName();
        this.itemPrice = display.getSalePrice();
        this.thumbnail = display.getItemMaster().getThumbnail();
        ItemDisplay.ItemDisplayOption option = entity.getItemOption();
        this.itemOptionId = option.getId();
        this.size = option.getSize();
        this.color = option.getColor();
        this.count = entity.getCount();
        Optional<DeliveryItem> deliveryItem = Optional.ofNullable(entity.getDeliveryItem());
        this.deliveryStatus = deliveryItem.map(d -> d.getDelivery().getStatus().getDescription()).orElse("");
        this.invoiceNum = deliveryItem.map(d -> d.getDelivery().getInvoiceNum()).orElse("");
        return this;
    }

}
