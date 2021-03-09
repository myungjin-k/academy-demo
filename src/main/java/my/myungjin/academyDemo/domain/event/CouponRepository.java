package my.myungjin.academyDemo.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, String> {
/*
    @Query(value = "select m.id as member_id, e.seq as event_seq" +
            "  from event e, member m" +
            "  where e.type = 'C'" +
            "  and e.rating like '%' || m.rating || '%'",
    nativeQuery = true)
    List<CouponProject> test();*/

    List<Coupon> findByCreateAtAfter(LocalDateTime createAt);

}
