package my.myungjin.academyDemo.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {

    // 회원 PK, 주문 PK, 배송 PK로 검색
    Optional<Delivery> findByOrderMemberIdAndOrderIdAndId(String orderMemberId, String orderId, String id);

    // 회원 PK, 주문 PK로 가져오기
    Delivery getByOrderMemberIdAndOrderId(String orderMemberId, String orderId);

    // 주문 엔티티로 가져오기, 생성일 내림차순
    List<Delivery> getAllByOrderOrderByCreateAtDesc(Order order);

    // 주문 엔티티, 특정 배송상태 제외하여 검색
    List<Delivery> findAllByOrderAndStatusIsNot(Order order, DeliveryStatus status);

    // 특정시점 이전에 생성되었고, 특정 배송상태인 배송정보 검색
    List<Delivery> findByCreateAtBeforeAndStatusIs(LocalDateTime createAt, DeliveryStatus status);

}
