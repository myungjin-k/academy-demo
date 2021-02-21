package my.myungjin.academyDemo.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String>, QuerydslPredicateExecutor<OrderItem> {

    // 주문 엔티티로 검색
    List<OrderItem> findAllByOrder(Order order);

    // 등록일시와 배송상태로 검색
    List<OrderItem> findByDeliveryItemDeliveryStatusEqualsAndCreateAtAfter(DeliveryStatus status, LocalDateTime createAt);


    /*@Query(value =
            "select t.* " +
            "from (" +
            "       select id.id as itemId, sum(oi.count) as saleCount " +
            "       from order_item oi " +
            "       join item_display_option ido on (ido.id = oi.item_id)" +
            "       join item_display id on (id.id = ido.display_id)" +
            "       join delivery_item di on (di.id = oi.delivery_item_id)" +
            "       join delivery d on (d.id = di.delivery_id)" +
            "       where d.status = 4 " +
            "       and oi.create_at > ?1 " +
            "       group by id.id" +
            ") t " +
            "order by t.saleCount desc",
            nativeQuery = true
    )
    List<TopSeller> findTopSellerItems(LocalDateTime crateAt);*/
}
