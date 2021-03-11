package my.myungjin.academyDemo.domain.event;

import my.myungjin.academyDemo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CouponRepository extends JpaRepository<Coupon, String> {

    @Query(value =
            "select et as event, m as member " +
                    "from EventTarget et " +
                    "inner join Member m on m.rating = et.rating " +
                    "left outer join Coupon c on c.member = m and c.event = et " +
                    "where et.event.type = :type and et.event.status = :status and et.event.startAt <= :today " +
                    "and c.id is null"
    )
    List<CouponProject> test(@Param("type") EventType type, @Param("status") EventStatus status, @Param("today") LocalDate today);

    List<Coupon> findByCreateAtAfter(LocalDateTime createAt);

    Set<Coupon> findByMember(Member member);

}
