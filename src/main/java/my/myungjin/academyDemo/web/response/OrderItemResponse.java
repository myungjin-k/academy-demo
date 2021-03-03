package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayOption;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.order.DeliveryItem;
import my.myungjin.academyDemo.domain.order.OrderItem;
import my.myungjin.academyDemo.domain.review.Review;

import java.util.Optional;

@Getter
public class OrderItemResponse {

    private String orderItemId;

    private String itemDisplayId;

    private String itemName;

    private int salePrice;

    private int itemPrice;

    private String thumbnail;

    private String itemOptionId;

    private String size;

    private String color;

    private int count;

    private String deliveryStatus;

    private String invoiceNum;

    private String deliveryId;

    private String reviewId;

    public OrderItemResponse of(OrderItem entity){
        this.orderItemId = entity.getId();
        ItemDisplay display = entity.getItemOption().getItemDisplay();
        this.itemDisplayId = display.getId();
        this.itemName = display.getItemDisplayName();
        this.salePrice = display.getSalePrice();
        ItemMaster master = display.getItemMaster();
        this.itemPrice = master.getPrice();
        this.thumbnail = master.getThumbnail();
        ItemDisplayOption option = entity.getItemOption();
        this.itemOptionId = option.getId();
        this.size = option.getSize();
        this.color = option.getColor();
        this.count = entity.getCount();
        Optional<DeliveryItem> deliveryItem = Optional.ofNullable(entity.getDeliveryItem());
        this.deliveryStatus = deliveryItem.map(d -> d.getDelivery().getStatus().getDescription()).orElse("");
        this.invoiceNum = deliveryItem.map(d -> d.getDelivery().getInvoiceNum()).orElse("");
        this.deliveryId = deliveryItem.map(d -> d.getDelivery().getId()).orElse("");
        this.reviewId = Optional.ofNullable(entity.getReview())
                .map(Review::getId)
                .orElse("");
        return this;
    }

}
