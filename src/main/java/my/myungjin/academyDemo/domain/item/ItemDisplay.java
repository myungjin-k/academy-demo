package my.myungjin.academyDemo.domain.item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import my.myungjin.academyDemo.domain.review.Review;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;


@Entity
@Table(name = "item_display")
@ToString(exclude = {"options", "reviews"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ItemDisplay {
    @Id @Getter
    @GeneratedValue(generator = "itemDisplayId")
    @GenericGenerator(name = "itemDisplayId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @Size(min = 1, max = 50)
    @Column(name = "item_display_name", nullable = false)
    private String itemDisplayName;

    @Getter
    @Column(name = "sale_price", nullable = false)
    private int salePrice;

    @Getter
    @Size(min = 1, max = 255)
    @Column(name = "size")
    private String size;

    @Getter
    @Size(min = 1, max = 255)
    @Column(name = "material")
    private String material;

    @Getter
    @Size(min = 1, max = 1000)
    @Column(name = "description")
    private String description;

    @Getter
    @Size(max = 1000)
    @Column(name = "notice")
    private String notice;

    @Getter
    @Column(name = "status", nullable = false, columnDefinition = "number default 0")
    @Convert(converter = ItemStatusConverter.class)
    private ItemStatus status;

    @Getter @Setter
    @Size(max = 255)
    @Column(name = "detail_image")
    private String detailImage;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter @Setter
    @JsonManagedReference
    @OneToOne
    // TODO History 저장
    @JoinColumn(name = "item_id", nullable = false)
    private ItemMaster itemMaster;

    @Getter @Setter
    @JsonBackReference
    @OneToMany(mappedBy = "itemDisplay", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Collection<ItemDisplayOption> options;

    @Getter @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Collection<Review> reviews;

    @Builder
    public ItemDisplay(String id,
                       @Size(min = 1, max = 50) String itemDisplayName, int salePrice, @Size(min = 1, max = 255) String material, @Size(min = 1, max = 255) String size,
                       @Size(min = 1, max = 1000) String description, @Size(min = 1, max = 1000) String notice,
                       ItemStatus status, @Size(min = 1, max = 255) String detailImage) {
        this.id = id;
        this.itemDisplayName = itemDisplayName;
        this.salePrice = salePrice;
        this.size = size;
        this.material = material;
        this.description = description;
        this.notice = notice;
        this.status = status;
        this.detailImage = detailImage;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void addOption(ItemDisplayOption option){
        options.add(option);
        option.setItemDisplay(this);
    }

    public void modify(ItemDisplay itemDisplay){
        this.salePrice = itemDisplay.getSalePrice();
        this.size = itemDisplay.getSize();
        this.material = itemDisplay.getMaterial();
        this.description = itemDisplay.getDescription();
        this.notice = itemDisplay.getNotice();
        this.status = itemDisplay.getStatus();
        this.updateAt = now();
    }

    public void updateSalePrice (int newPrice) {
        salePrice = newPrice;
    }
}