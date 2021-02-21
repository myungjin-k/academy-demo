package my.myungjin.academyDemo.domain.order;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class TopSeller {

    private String itemId;

    private int saleCount;

}
