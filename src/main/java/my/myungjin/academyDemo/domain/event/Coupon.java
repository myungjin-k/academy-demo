package my.myungjin.academyDemo.domain.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import my.myungjin.academyDemo.domain.member.Member;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

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
    @ManyToOne
    @JoinColumn(name = "event_seq", nullable = false)
    private Event event;

    @Getter
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Getter
    @Column(name = "used_yn", columnDefinition = "char default 'N'")
    private char usedYn;

    @Getter
    @Column(name = "expired_yn", columnDefinition = "char default 'N'")
    private char expiredYn;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Builder
    public Coupon(Event event, Member member) {
        this.event = event;
        this.member = member;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void use(){
        usedYn = 'Y';
    }

    public void expire(){
        expiredYn = 'Y';
    }
}
