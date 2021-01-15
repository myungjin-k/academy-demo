package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemStatus;

@Getter
@ToString
@AllArgsConstructor
public class ItemDisplayRequest {

    private int salePrice;

    private String size;

    private String material;

    private String description;

    private String notice;

    private String status;

    public ItemStatus getStatus(){
        return ItemStatus.valueOf(status);
    }

    public ItemDisplay newItemDisplay(){
        return ItemDisplay.builder()
                .salePrice(salePrice)
                .size(size)
                .material(material)
                .description(description)
                .notice(notice)
                .status(ItemStatus.valueOf(status))
                .build();
    }

    public ItemDisplay toItemDisplay(Id<ItemDisplay, String> displayId){
        return ItemDisplay.builder()
                .id(displayId.value())
                .salePrice(salePrice)
                .size(size)
                .material(material)
                .description(description)
                .notice(notice)
                .status(ItemStatus.valueOf(status))
                .build();
    }
}
