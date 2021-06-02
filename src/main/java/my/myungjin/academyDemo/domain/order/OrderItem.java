package my.myungjin.academyDemo.domain.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemDisplayOption;
import my.myungjin.academyDemo.domain.review.Review;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private ItemDisplayOption itemOption;


    @Setter @Getter
    @JsonManagedReference
    @OneToMany(mappedBy = "orderItem", fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    //@JoinColumn(name = "delivery_item_id")
    private List<DeliveryItem> deliveryItems = new ArrayList<>();

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

    public Optional<DeliveryItem> getLatestDeliveryItem(){
        return deliveryItems.stream()
                .filter(deliveryItem -> !DeliveryStatus.DELETED.equals(deliveryItem.getDelivery().getStatus()))
                .reduce((deliveryItem, deliveryItem2) -> deliveryItem.getDelivery().getCreateAt().isAfter(deliveryItem2.getCreateAt()) ? deliveryItem : deliveryItem2);
    }
}
