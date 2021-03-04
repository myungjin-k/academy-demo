package my.myungjin.academyDemo.domain.event;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "event")
@ToString(exclude = {"items"})
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

    @Builder
    public Event(long seq, String name, EventType type, EventStatus status, int ratio, int amount, LocalDate startAt, LocalDate endAt) {
        this.name = name;
        this.seq = seq;
        this.type = type;
        this.status = status;
        this.ratio = ratio;
        this.amount = amount;
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

    public void modify(String name, EventType type, int amount, EventStatus status, LocalDate startAt, LocalDate endAt){
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void on(){
        status = EventStatus.ON;
    }
}
