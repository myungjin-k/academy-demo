package my.myungjin.academyDemo.domain.item;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;


@Entity
@Table(name = "item_master")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ItemMaster {
    @Id @Getter
    private String id;

    @Getter
    @Size(min = 1, max = 50)
    @Column(name = "item_name", nullable = false, unique = true)
    private String itemName;

    @Getter
    @Size(min = 1, max = 255)
    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Getter
    @Column(name = "price", nullable = false)
    private int price;

    @Getter
    @Size(min = 1, max = 255)
    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Getter
    @Column(name = "status", nullable = false, columnDefinition = "number default 0")
    @Convert(converter = ItemStatusConverter.class)
    private ItemStatus status;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter
    @OneToMany(mappedBy = "masterId", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) //JOIN
    private Collection<ItemOption> options;

    @Builder
    public ItemMaster(String id, @Size(min = 1, max = 50) String itemName,
                      @Size(min = 1, max = 255) String categoryId,
                      int price, String thumbnail, ItemStatus status, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.itemName = itemName;
        this.categoryId = categoryId;
        this.price = price;
        this.thumbnail = thumbnail;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
