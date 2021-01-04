package my.myungjin.academyDemo.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class ItemOptionResponse {

    private String optionId;

    private String color;

    private String size;

    private boolean isSoldOut;

}
