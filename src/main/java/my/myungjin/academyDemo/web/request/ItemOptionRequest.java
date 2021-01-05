package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.util.Util;

@ToString
@Getter
@AllArgsConstructor
public class ItemOptionRequest {

    private String size;

    private String color;

    public ItemMaster.ItemOption newItemOption(){
        return ItemMaster.ItemOption.builder()
                .size(size)
                .color(color)
                .build();
    }

}
