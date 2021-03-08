package my.myungjin.academyDemo.domain.item;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "item_display_price_history")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ItemDisplayPriceHistory {

    @Id
    @Getter
    @GeneratedValue(generator = "itemDisplayPriceHistoryId")
    @GenericGenerator(name = "itemDisplayPriceHistoryId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @Column(name = "sale_price", nullable = false)
    private int salePrice;

    @Getter
    @Column(name = "ref")
    private String ref;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDisplay item;

    @Getter
    @Column(name = "seq", nullable = false)
    private int seq;

    @Builder
    public ItemDisplayPriceHistory(int salePrice, ItemDisplay itemDisplay, int seq, String ref) {
        this.salePrice = salePrice;
        this.item = itemDisplay;
        this.seq = seq;
        this.ref = ref;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }
}
