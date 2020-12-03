package my.myungjin.academyDemo.domain.item;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Entity
@Table(name = "item_display")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ItemDisplay {
    @Id
    private String id;

    @Size(min = 1, max = 50)
    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "sale_price", nullable = false)
    private int salePrice;

    @Size(min = 1, max = 255)
    @Column(name = "material")
    private String material;

    @Size(min = 1, max = 1000)
    @Column(name = "description")
    private String description;

    @Size(min = 1, max = 1000)
    @Column(name = "notice")
    private String notice;

    @Size(min = 1, max = 255)
    @Column(name = "detail_image")
    private String detailImage;

    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;


}
