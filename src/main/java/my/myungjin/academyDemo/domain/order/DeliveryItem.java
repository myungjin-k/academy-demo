package my.myungjin.academyDemo.domain.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "delivery_item")
@ToString(exclude = "orderItem")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class DeliveryItem {

    @Id
    @Getter
    @GeneratedValue(generator = "deliveryItemId")
    @GenericGenerator(name = "deliveryItemId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @Column(name = "count", nullable = false)
    private int count;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Getter @Setter
    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDisplay.ItemDisplayOption itemOption;

    @Getter @Setter
    @JsonBackReference
    @OneToOne(mappedBy = "deliveryItem", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private OrderItem orderItem;

    public DeliveryItem(int count) {
        this.count = count;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void modifyCount(int count){
        this.count = count;
    }
}
