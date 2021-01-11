package my.myungjin.academyDemo.web.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.item.ItemMaster;

@ToString
@Setter
@Getter
@RequiredArgsConstructor
public class ItemMasterRequest {

    private String categoryId;

    private String itemName;

    private int price;

    public ItemMaster newItemMaster(){
        return ItemMaster.builder()
                .itemName(itemName)
                .price(price)
                .build();
    }
}
