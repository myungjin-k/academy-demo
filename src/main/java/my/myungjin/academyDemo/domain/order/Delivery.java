package my.myungjin.academyDemo.domain.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "delivery")
@ToString(exclude = {"items", "receivedDeliveryStatuses"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id @Getter
    @GeneratedValue(generator = "deliveryId")
    @GenericGenerator(name = "deliveryId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
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
    @Column(name = "status", nullable = false, columnDefinition = "number default 1")
    @Convert(converter = DeliveryStatusConverter.class)
    private DeliveryStatus status;

    @Getter
    @Column(name = "invoice_num", nullable = false)
    private String invoiceNum;

    @Getter
    @Size(max = 100)
    @Column(name = "message", nullable = false)
    private String message;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter @Setter
    @Column(name = "ext_delivery_id", unique = true)
    private String extDeliveryId;

    @Getter @Setter
    @JsonBackReference
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private List<ReceivedDeliveryStatus> receivedDeliveryStatuses;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Getter @Setter
    @JsonManagedReference
    @OneToMany(mappedBy = "delivery", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DeliveryItem> items = new ArrayList<>();

    @Builder
    public Delivery(String id, @Size(min = 1, max = 10) String receiverName, @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 010-0000-0000 형태여야 합니다.") String receiverTel, String receiverAddr1, String receiverAddr2, DeliveryStatus status, String invoiceNum, @Size(max = 100) String message) {
        this.id = id;
        this.receiverName = receiverName;
        this.receiverTel = receiverTel;
        this.receiverAddr1 = receiverAddr1;
        this.receiverAddr2 = receiverAddr2;
        this.status = status;
        this.invoiceNum = invoiceNum;
        this.message = message;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void addItem(DeliveryItem item){
        items.add(item);
        item.setDelivery(this);
    }

    public void modify(String receiverName, String receiverTel, String receiverAddr1, String receiverAddr2, String message){
        this.receiverName = receiverName;
        this.receiverTel = receiverTel;
        this.receiverAddr1 = receiverAddr1;
        this.receiverAddr2 = receiverAddr2;
        this.message = message;
        this.updateAt = now();
    }

    public void updateStatus(DeliveryStatus status){
        this.status = status;
    }

    public void updateInvoice(String invoiceNum){
        this.invoiceNum = invoiceNum;
    }

    public void updateAddress(String addr1, String addr2){
        this.receiverAddr1 = addr1;
        this.receiverAddr2 = addr2;
    }

    public void addReceivedDeliveryStatus(ReceivedDeliveryStatus status){
        this.receivedDeliveryStatuses.add(status);
        status.setDelivery(this);
    }

}
