package my.myungjin.academyDemo.domain.receiver;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemStatusConverter;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.domain.order.DeliveryStatusConverter;
import my.myungjin.academyDemo.domain.order.Order;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "delivery")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Delivery {

    @Id
    @Getter
    private String id;

    @Getter
    @Size(min = 1, max = 10)
    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Getter
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 010-0000-0000 형태여야 합니다.")
    @Column(name = "receiver_tel" , nullable = false)
    private String receiverTel;

    @Getter
    @Column(name = "receiver_addr1", nullable = false)
    private String receiverAddr1;

    @Getter
    @Column(name = "receiver_addr2", nullable = false)
    private String receiverAddr2;

    @Getter
    @Column(name = "status", nullable = false)
    @Convert(converter = DeliveryStatusConverter.class)
    private DeliveryStatus status;

    @Getter
    @Column(name = "invoice_num", nullable = false)
    private String invoiceNum;

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

    @Builder
    public Delivery(String id, @Size(min = 1, max = 10) String receiverName, @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 010-0000-0000 형태여야 합니다.") String receiverTel, String receiverAddr1, String receiverAddr2, DeliveryStatus status, String invoiceNum) {
        this.id = id;
        this.receiverName = receiverName;
        this.receiverTel = receiverTel;
        this.receiverAddr1 = receiverAddr1;
        this.receiverAddr2 = receiverAddr1;
        this.status = status;
        this.invoiceNum = invoiceNum;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void modify(String receiverName, String receiverTel, String receiverAddr1, String receiverAddr2){
        this.receiverName = receiverName;
        this.receiverTel = receiverTel;
        this.receiverAddr1 = receiverAddr1;
        this.receiverAddr2 = receiverAddr2;
        this.updateAt = now();
    }
}
