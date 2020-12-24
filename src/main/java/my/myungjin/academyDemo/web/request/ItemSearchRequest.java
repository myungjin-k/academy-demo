package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
public class ItemSearchRequest {

    private String itemName;

    private LocalDate start;

    private LocalDate end;

}
