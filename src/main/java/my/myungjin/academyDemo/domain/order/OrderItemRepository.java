package my.myungjin.academyDemo.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String>, QuerydslPredicateExecutor<OrderItem> {

    // 주문 엔티티로 검색
    List<OrderItem> findAllByOrder(Order order);

}
