package my.myungjin.academyDemo.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {

    Optional<Delivery> findByOrder_Member_idAndOrder_idAndId(String orderMemberId, String orderId, String id);

    List<Delivery> findByOrder_Member_idAndOrder_id(String orderMemberId, String orderId);

    List<Delivery> findByOrder(Order order);
}
