package my.myungjin.academyDemo.domain.event;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import my.myungjin.academyDemo.domain.member.Rating;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "event")
@ToString(exclude = {"items", "targets"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "seq")
public class Event {

    @Id @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Getter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Column(name = "type", nullable = false)
    @Convert(converter = EventTypeConverter.class)
    private EventType type;

    @Getter
    @Column(name = "status",
            columnDefinition = "number default 0")
    @Convert(converter = EventStatusConverter.class)
    private EventStatus status;

    @Getter
    @Column(name = "discount_ratio")
    private int ratio;

    @Getter
    @Column(name = "discount_amount")
    private int amount;

    @Getter
    @Column(name = "min_amount")
    private int minPurchaseAmount;

    @Getter
    @Column(name = "start_at",
            columnDefinition = "datetime default current_date")
    private LocalDate startAt;

    @Getter
    @Column(name = "end_at",
            columnDefinition = "datetime default current_date")
    private LocalDate endAt;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false, nullable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter @Setter
    @JsonManagedReference
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<EventItem> items = new ArrayList<>();

    @Getter @Setter
    @JsonManagedReference
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<EventTarget> targets = new HashSet<>();

    @Builder
    public Event(long seq, String name, EventType type, EventStatus status, int ratio, int amount, Set<Rating> ratings,
                 int minPurchaseAmount, LocalDate startAt, LocalDate endAt) {
        this.name = name;
        this.seq = seq;
        this.type = type;
        this.status = status;
        this.ratio = ratio;
        this.amount = amount;
        this.minPurchaseAmount = minPurchaseAmount;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void addItem(EventItem eventItem){
        items.add(eventItem);
        eventItem.setEvent(this);
    }

    public void modify(Event event){
        this.name = event.getName();
        this.type = event.getType();
        this.ratio = event.getRatio();
        this.amount = event.getAmount();
        this.minPurchaseAmount = event.getMinPurchaseAmount();
        this.status = event.getStatus();
        this.startAt = event.getStartAt();
        this.endAt = event.getEndAt();
    }

}
