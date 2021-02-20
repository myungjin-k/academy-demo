package my.myungjin.academyDemo.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceivedDeliveryStatusRepository extends JpaRepository<ReceivedDeliveryStatus, String> {

    List<ReceivedDeliveryStatus> findByApplyYnEquals(char applyYn);

}
