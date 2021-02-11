package my.myungjin.academyDemo.service.review;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Admin;
import my.myungjin.academyDemo.domain.member.AdminRepository;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.domain.review.ReviewComment;
import my.myungjin.academyDemo.domain.review.ReviewCommentRepository;
import my.myungjin.academyDemo.domain.review.ReviewRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;

    private final ReviewRepository reviewRepository;

    private final AdminRepository adminRepository;

    @Transactional
    public List<ReviewComment> findAllByReview(@Valid Id<Review, String> reviewId){
        return reviewRepository.findById(reviewId.value())
                .map(review -> {
                    List<ReviewComment> comments = reviewCommentRepository.findAllByReviewId(review.getId());
                    review.setComments(comments);
                    return comments;
                })
                .orElseThrow(() -> new NotFoundException(Review.class, reviewId));
    }

    @Transactional
    public Optional<ReviewComment> findById(@Valid Id<ReviewComment, String> commentId){
        return reviewCommentRepository.findById(commentId.value());
    }

    @Transactional
    public ReviewComment write(@Valid Id<Review, String> reviewId, @Valid Id<Admin, String> adminId, @Valid ReviewComment newComment){
        final Admin admin = adminRepository.findById(adminId.value()).orElseThrow(() -> new NotFoundException(Admin.class, adminId));
        return reviewRepository.findById(reviewId.value())
                .map(review -> {
                    newComment.setWriter(admin);
                    newComment.setReview(review);
                    return save(newComment);
                }).orElseThrow(() -> new NotFoundException(Review.class, reviewId));
    }

    @Transactional
    public ReviewComment modifyContent(@Valid Id<ReviewComment, String> commentId, @NotBlank String content){
        return findById(commentId)
                .map(reviewComment -> {
                    reviewComment.modify(content);
                    return save(reviewComment);
                }).orElseThrow(() -> new NotFoundException(ReviewComment.class, commentId));
    }

    @Transactional
    public ReviewComment delete(@Valid Id<ReviewComment, String> commentId){
        return findById(commentId)
                .map(reviewComment -> {
                    reviewCommentRepository.delete(reviewComment);
                    return reviewComment;
                }).orElseThrow(() -> new NotFoundException(ReviewComment.class, commentId));
    }

    private ReviewComment save(ReviewComment comment){
        return reviewCommentRepository.save(comment);
    }

}
