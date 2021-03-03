package my.myungjin.academyDemo.domain.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "event_item")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class EventItem {

    @Id
    @Getter
    @GeneratedValue(generator = "eventItemId")
    @GenericGenerator(name = "eventItemId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "event_seq", nullable = false)
    private Event event;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDisplay item;


    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Builder
    public EventItem(Event event, ItemDisplay item) {
        this.event = event;
        this.item = item;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }
}
