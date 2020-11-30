package my.myungjin.academyDemo.domain.item;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "item_master")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ItemMaster {
    @Id
    private String id;

    @Size(min = 1, max = 50)
    @Column(name = "item_name", nullable = false, unique = true)
    private String itemName;

    @Size(min = 1, max = 255)
    @Column(name = "main_category_id", nullable = false)
    private String mainCategoryId;

    @Size(min = 1, max = 255)
    @Column(name = "sub_category_id", nullable = false)
    private String subCategoryId;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "status", nullable = false, columnDefinition = "number default 0")
    @Convert(converter = ItemStatusConverter.class)
    private ItemStatus status;

    @Column(name = "detail_image_url")
    private String detailImgUrl;

    @Column(name = "additional_info")
    private String addInfo;

    @Column(name = "comment")
    private String comment;

    @Column(name = "notice")
    private String notice;

    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at", insertable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime updateAt;

    @Builder
    public ItemMaster(String id, @Size(min = 1, max = 50) String itemName,
                      @Size(min = 1, max = 255) String mainCategoryId, @Size(min = 1, max = 255) String subCategoryId,
                      int price, ItemStatus status, String detailImgUrl, String addInfo, String comment, String notice,
                      LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.itemName = itemName;
        this.mainCategoryId = mainCategoryId;
        this.subCategoryId = subCategoryId;
        this.price = price;
        this.status = status;
        this.detailImgUrl = detailImgUrl;
        this.addInfo = addInfo;
        this.comment = comment;
        this.notice = notice;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
