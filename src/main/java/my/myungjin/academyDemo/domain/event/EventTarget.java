package my.myungjin.academyDemo.domain.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.member.Rating;
import my.myungjin.academyDemo.domain.member.RatingConverter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "event_target")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class EventTarget {

    @Id
    @Getter
    @GeneratedValue(generator = "eventTargetId")
    @GenericGenerator(name = "eventTargetId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "event_seq", nullable = false)
    private Event event;

    @Getter
    @Column(name = "rating", nullable = false)
    @Convert(converter = RatingConverter.class)
    private Rating rating;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Builder
    public EventTarget(Event event, Rating rating) {
        this.event = event;
        this.rating = rating;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

}
