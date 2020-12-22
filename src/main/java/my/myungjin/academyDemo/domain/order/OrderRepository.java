package my.myungjin.academyDemo.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByMember_idAndId(String memberId, String id);

    Page<Order> findAllByMember_id(String memberId, Pageable pageable);

}
