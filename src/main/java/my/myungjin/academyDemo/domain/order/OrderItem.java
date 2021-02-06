package my.myungjin.academyDemo.domain.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.review.Review;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "order_item")
@ToString(exclude = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class OrderItem {

    @Id @Getter
    @GeneratedValue(generator = "orderItemId")
    @GenericGenerator(name = "orderItemId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
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
    @JoinColumn(name = "order_id")
    private Order order;

    @Setter @Getter
    //@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDisplay.ItemDisplayOption itemOption;


    @Setter @Getter
    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "delivery_item_id")
    private DeliveryItem deliveryItem;

    @Setter @Getter
    @JsonManagedReference
    @OneToOne(mappedBy = "orderItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Review review;

    public OrderItem(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

}
