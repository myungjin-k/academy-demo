package my.myungjin.academyDemo.domain.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.member.Member;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "coupon")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Coupon {

    @Id
    @Getter
    @GeneratedValue(generator = "couponId")
    @GenericGenerator(name = "couponId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "event_target_id", nullable = false)
    private EventTarget event;

    @Getter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Getter
    @Column(name = "used_yn", columnDefinition = "char default 'N'", nullable = false)
    private char usedYn;

    @Getter
    @Column(name = "expired_yn", columnDefinition = "char default 'N'", nullable = false)
    private char expiredYn;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Builder
    public Coupon(EventTarget event, Member member) {
        this.event = event;
        this.member = member;
        this.expiredYn = 'N';
        this.usedYn = 'N';
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void use(){
        usedYn = 'Y';
        updateAt = now();
    }

    public void unused(){
        usedYn = 'N';
        updateAt = now();
    }

    public void expire(){
        expiredYn = 'Y';
        updateAt = now();
    }
}
