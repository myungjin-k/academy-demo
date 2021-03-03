package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemMaster;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemDetailResponse {

    private String displayId;

    private String itemName;

    private int originalPrice;

    private int itemPrice;

    private String thumbnail;

    private String sizeInfo;

    private String material;

    private String description;

    private String notice;

    private String detailImage;

    private List<ItemOptionResponse> options = new ArrayList<>();

    public ItemDetailResponse of(ItemDisplay entity){
        this.displayId = entity.getId();
        this.itemName = entity.getItemDisplayName();
        ItemMaster master = entity.getItemMaster();
        this.thumbnail = master.getThumbnail();
        this.originalPrice = master.getPrice();
        this.itemPrice = entity.getSalePrice();
        this.sizeInfo = entity.getSize();
        this.material = entity.getMaterial();
        this.description = entity.getDescription();
        this.notice = entity.getNotice();
        this.detailImage = entity.getDetailImage();
        for(ItemDisplayOption option : entity.getOptions()){
            this.options.add(
                    new ItemOptionResponse(
                        option.getId(),
                        option.getColor(),
                        option.getSize(),
                        option.getStatus().getValue() == 2
                    )
            );
        }
        return this;
    }


}
