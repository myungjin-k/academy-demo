package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
public class ItemMasterSearchRequest {

    private String itemName;

    private LocalDate start;

    private LocalDate end;

}
