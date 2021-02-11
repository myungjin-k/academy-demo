package my.myungjin.academyDemo.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, String> {

    // 리뷰 PK로 조회
    List<ReviewComment> findAllByReviewId(String reviewId);

    // 리뷰 PK와 코멘트 PK로 조회
    Optional<ReviewComment> findByReviewIdAndId(String reviewId, String commentId);
}
