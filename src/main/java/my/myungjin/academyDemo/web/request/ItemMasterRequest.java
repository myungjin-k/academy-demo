package my.myungjin.academyDemo.web.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.util.Util;

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
