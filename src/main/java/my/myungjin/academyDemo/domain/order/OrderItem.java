package my.myungjin.academyDemo.domain.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "order_item")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class OrderItem {

    @Id
    @Getter
    private String id;

    @Getter
    @Column(name = "count", nullable = false)
    private int count;

    @Getter @Setter
    private String reviewId;

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
    @OneToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDisplay.ItemDisplayOption itemOption;

    public OrderItem(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

}