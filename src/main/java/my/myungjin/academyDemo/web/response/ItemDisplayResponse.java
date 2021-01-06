package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemMaster;

@Getter
public class ItemDisplayResponse {

    private String displayId;

    private String itemName;

    private int originalPrice;

    private int itemPrice;

    private String thumbnail;

    public ItemDisplayResponse of(ItemDisplay entity){
        this.displayId = entity.getId();
        this.itemName = entity.getItemDisplayName();
        this.itemPrice = entity.getSalePrice();
        ItemMaster master = entity.getItemMaster();
        this.originalPrice = master.getPrice();
        this.thumbnail = master.getThumbnail();
        return this;
    }
}
