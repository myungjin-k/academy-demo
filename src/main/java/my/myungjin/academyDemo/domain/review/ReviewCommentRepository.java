package my.myungjin.academyDemo.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, String> {

    // 리뷰 PK로 조회
    List<ReviewComment> findAllByReviewId(String reviewId);
}
