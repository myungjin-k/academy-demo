package my.myungjin.academyDemo.domain.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "received_delivery_status")
@EqualsAndHashCode(of = {"delivery", "seq"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceivedDeliveryStatus {

    @Id @Getter
    @GeneratedValue(generator = "extDeliveryId")
    @GenericGenerator(name = "extDeliveryId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter @Setter
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "ext_delivery_id", referencedColumnName = "ext_delivery_id", nullable = false)
    private Delivery delivery;

    @Getter
    @Column(name = "seq", nullable = false)
    private long seq;

    @Getter
    @Column(name = "status", nullable = false)
    @Convert(converter = DeliveryStatusConverter.class)
    private DeliveryStatus status;

    @Getter
    @Column(name = "apply_yn", columnDefinition = "char default 'N'")
    private char applyYn;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Builder
    public ReceivedDeliveryStatus(Delivery delivery, long seq) {
        this.delivery = delivery;
        this.seq = seq;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void apply(){
        this.applyYn = 'Y';
        this.updateAt = now();
    }

}
