package my.myungjin.academyDemo.domain.member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Table(name = "reserves_history")
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ReservesHistory {

    @Id @Getter
    @GeneratedValue(generator = "reservesHistoryId")
    @GenericGenerator(name = "reservesHistoryId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @Column(name = "amount", nullable = false)
    private int amount;

    @Getter
    @Column(name = "type", nullable = false, updatable = false)
    @Convert(converter = ReservesTypeConverter.class)
    private ReservesType type;

    // TODO Id Converter ?
    @Getter
    @Column(name = "ref_id")
    private String refId;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public ReservesHistory(int amount, ReservesType type, String refId) {
        this.amount = amount;
        this.type = type;
        this.refId = refId;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

}
