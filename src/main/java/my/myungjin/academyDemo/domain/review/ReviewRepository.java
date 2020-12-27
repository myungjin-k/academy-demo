package my.myungjin.academyDemo.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, String> {

    Optional<Review> findByOrderItem_idAndMember_id(String itemId, String memberId);

    Page<Review> findByItem_id(String itemId, Pageable pageable);

}
