package my.myungjin.academyDemo.domain.order;

import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "top_seller")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class TopSeller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "topSellerId")
    @GenericGenerator(name = "topSellerId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @OneToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDisplay item;


    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    public TopSeller(ItemDisplay item) {
        this.item = item;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }
}
