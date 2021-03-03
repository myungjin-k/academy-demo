package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.item.ItemOption;

@ToString
@Getter
@AllArgsConstructor
public class ItemOptionRequest {

    private String size;

    private String color;

    public ItemOption newItemOption(){
        return ItemOption.builder()
                .size(size.isBlank() ? "ONE SIZE" : size)
                .color(color.isBlank() ? "ONE COLOR" : color)
                .build();
    }

}
