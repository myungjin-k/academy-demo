package my.myungjin.academyDemo.domain.order;

import my.myungjin.academyDemo.domain.item.ItemDisplay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String>{

    // 회원 PK와 주문 PK로 검색
    Optional<Order> findByMemberIdAndId(String memberId, String id);

    // 회원 PK로 검색
    Page<Order> findAllByMemberId(String memberId, Pageable pageable);

}
