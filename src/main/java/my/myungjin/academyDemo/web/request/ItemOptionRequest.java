package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.item.ItemOption;
import my.myungjin.academyDemo.util.Util;

@ToString
@Getter
@AllArgsConstructor
public class ItemOptionRequest {

    private String size;

    private String color;

    public ItemOption newItemOption(){
        return ItemOption.builder()
                .id(Util.getUUID())
                .size(size)
                .color(color)
                .build();
    }

}
