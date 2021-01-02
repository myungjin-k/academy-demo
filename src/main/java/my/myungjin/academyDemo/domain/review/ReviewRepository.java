package my.myungjin.academyDemo.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, String> {

    // 회원 PK와 리뷰 PK로 검색
    Optional<Review> findByMemberIdAndId(String memberId, String id);

    // 주문상품 PK와 회원 PK로 검색
    Optional<Review> findByOrderItemIdAndMemberId(String itemId, String memberId);

    // 전시상품 PK로 검색
    Page<Review> findByItemId(String itemId, Pageable pageable);

}
