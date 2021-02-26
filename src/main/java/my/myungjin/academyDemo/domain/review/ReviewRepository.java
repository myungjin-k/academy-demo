package my.myungjin.academyDemo.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, String>, QuerydslPredicateExecutor<Review> {


    // 전체 리뷰 조회(최근순)
    List<Review> findAllByOrderByCreateAtDesc();

    // 회원 PK와 리뷰 PK로 검색
    Optional<Review> findByMemberIdAndId(String memberId, String id);

    // 주문상품 PK와 회원 PK로 검색
    Optional<Review> findByOrderItemIdAndMemberId(String itemId, String memberId);

    // 전시상품 PK로 검색
    List<Review> findByItemId(String itemId);

    // 리뷰 PK와 작성자 회원ID로 검색
    Page<Review> findByIdOrMemberUserId(String reviewId, String writerUserId, Pageable pageable);

}
