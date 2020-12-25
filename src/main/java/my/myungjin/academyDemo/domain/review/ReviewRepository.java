package my.myungjin.academyDemo.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, String> {

    Optional<Review> findByItem_idAndMember_id(String itemId, String memberId);

}
