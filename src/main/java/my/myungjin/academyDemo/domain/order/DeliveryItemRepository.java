package my.myungjin.academyDemo.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, String> {

    List<DeliveryItem> findAllByDelivery (Delivery delivery);

    Optional<DeliveryItem> findByDelivery_idAndId (String deliveryId, String deliveryItemId);

    boolean existsByDelivery_idAndItemOption_id (String deliveryId, String itemId);

}
