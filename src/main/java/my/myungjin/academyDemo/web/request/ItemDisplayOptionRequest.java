package my.myungjin.academyDemo.web.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ItemDisplayOptionRequest {

    private String size;

    private String color;

    private String status;

}
