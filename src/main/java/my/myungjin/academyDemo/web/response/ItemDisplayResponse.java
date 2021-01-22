package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemMaster;

@Getter
public class ItemDisplayResponse {

    private String displayId;

    private String categoryId;

    private String categoryName;

    private String parentCategoryId;

    private String parentCategoryName;

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
        CommonCode category = master.getCategory();
        this.categoryId = category.getId();
        this.categoryName = category.getNameKor();
        CodeGroup parentCategory = category.getCodeGroup();
        if(!"C".equals(parentCategory.getCode())){
            this.parentCategoryId = parentCategory.getId();
            this.parentCategoryName = parentCategory.getNameKor();
        }
        return this;
    }
}
