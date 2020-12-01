package my.myungjin.academyDemo.domain.item;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;


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

    @Size(min = 1, max = 255)
    @Column(name = "detail_image_url", nullable = false)
    private String detailImgUrl;

    @Column(name = "status", nullable = false, columnDefinition = "number default 0")
    @Convert(converter = ItemStatusConverter.class)
    private ItemStatus status;

    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "masterId", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) //JOIN
    private Collection<ItemOption> options;

    @Builder
    public ItemMaster(String id, @Size(min = 1, max = 50) String itemName,
                      @Size(min = 1, max = 255) String mainCategoryId, @Size(min = 1, max = 255) String subCategoryId,
                      int price, String detailImgUrl, ItemStatus status, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.itemName = itemName;
        this.mainCategoryId = mainCategoryId;
        this.subCategoryId = subCategoryId;
        this.price = price;
        this.detailImgUrl = detailImgUrl;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
