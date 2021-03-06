package my.myungjin.academyDemo.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryItemRepository extends JpaRepository<DeliveryItem, String>{

    // 배송정보 엔티티로 검색
    List<DeliveryItem> findAllByDelivery (Delivery delivery);

    // 배송정보 PK와 배송상품 PK로 검색
    Optional<DeliveryItem> findByDeliveryIdAndId (String deliveryId, String deliveryItemId);

    // 배송정보 PK와 상품옵션 PK로 검색
    boolean existsByDeliveryIdAndItemOptionId (String deliveryId, String itemId);

    // 주문 엔티티와 상품옵션 PK로 검색, 특정배송상태 제외, 생성일 오름차순
    List<DeliveryItem> findAllByDeliveryOrderAndItemOptionIdAndDeliveryStatusNotOrderByCreateAtDesc(Order order, String itemId, DeliveryStatus exceptStatus);

}
