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

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;

    private final ReviewRepository reviewRepository;

    private final AdminRepository adminRepository;

    public List<ReviewComment> findAllByReview(@Valid Id<Review, String> reviewId){
        return reviewRepository.findById(reviewId.value())
                .map(review -> reviewCommentRepository.findAllByReviewId(review.getId()))
                .orElseThrow(() -> new NotFoundException(Review.class, reviewId));
    }

    public ReviewComment write(@Valid Id<Review, String> reviewId, @Valid Id<Admin, String> adminId, @Valid ReviewComment newComment){
        final Admin admin = adminRepository.findById(adminId.value()).orElseThrow(() -> new NotFoundException(Admin.class, adminId));
        return reviewRepository.findById(reviewId.value())
                .map(review -> {
                    newComment.setWriter(admin);
                    newComment.setReview(review);
                    return save(newComment);
                }).orElseThrow(() -> new NotFoundException(Review.class, reviewId));
    }

    private ReviewComment save(ReviewComment comment){
        return reviewCommentRepository.save(comment);
    }

}
