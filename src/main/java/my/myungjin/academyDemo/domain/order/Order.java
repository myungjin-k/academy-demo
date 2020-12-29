package my.myungjin.academyDemo.domain.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import my.myungjin.academyDemo.domain.member.Member;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "order_master")
@ToString(exclude = {"deliveries"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Order {

    @Id
    @Getter
    private String id;

    @Getter @Setter
    @Column(name = "abbr_items_name", nullable = false)
    private String abbrOrderItems;

    @Getter @Setter
    @Column(name = "total_amount" , nullable = false)
    private int totalAmount;

    @Getter
    @Size(min = 1, max = 10)
    @Column(name = "order_name", nullable = false)
    private String orderName;

    @Getter
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 010-0000-0000 형태여야 합니다.")
    @Column(name = "order_tel" , nullable = false)
    private String orderTel;

    @Getter
    @Column(name = "order_addr1", nullable = false)
    private String orderAddr1;

    @Getter
    @Column(name = "order_addr2", nullable = false)
    private String orderAddr2;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Getter @Setter
    @JsonManagedReference
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Delivery> deliveries = new ArrayList<>();

    @Builder
    public Order(String id, int totalAmount, @Size(min = 1, max = 10) String orderName, @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 010-0000-0000 형태여야 합니다.") String orderTel, String orderAddr1, String orderAddr2) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.orderName = orderName;
        this.orderTel = orderTel;
        this.orderAddr1 = orderAddr1;
        this.orderAddr2 = orderAddr2;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void addDelivery(Delivery delivery){
        deliveries.add(delivery);
        delivery.setOrder(this);
    }

    public void modify(String orderName, String orderTel, String orderAddr1, String orderAddr2){
        this.orderName = orderName;
        this.orderTel = orderTel;
        this.orderAddr1 = orderAddr1;
        this.orderAddr2 = orderAddr2;
        this.updateAt = now();
    }


}
