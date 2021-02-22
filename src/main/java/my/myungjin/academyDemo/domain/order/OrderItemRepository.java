package my.myungjin.academyDemo.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String>, QuerydslPredicateExecutor<OrderItem> {

    // 주문 엔티티로 검색
    List<OrderItem> findAllByOrder(Order order);

    /*@Query(value = "select item.itemOption.itemDisplay.id as itemId " +
            "from OrderItem item " +
            "where item.deliveryItem.delivery.status = :status and item.createAt >= :createAt " +
            "group by item.itemOption.itemDisplay " +
            "order by sum(item.count)"
    )
    List<TopSeller> findTopSellerItems(@Param("status") DeliveryStatus status, @Param("createAt") LocalDateTime createAt);*/
}
