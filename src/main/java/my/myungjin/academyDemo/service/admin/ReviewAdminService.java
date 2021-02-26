package my.myungjin.academyDemo.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.ReservesHistory;
import my.myungjin.academyDemo.domain.member.ReservesType;
import my.myungjin.academyDemo.domain.review.*;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewAdminService {

    private final ReviewRepository reviewRepository;

    private final ReviewCommentRepository reviewCommentRepository;

    @Transactional
    public List<Review> search(String reviewId, String writerUserId, String replyStatus){
        return (ArrayList<Review>) reviewRepository.findAll(ReviewPredicate.searchByIdAndWriterUserId(reviewId, writerUserId, replyStatus));
    }

    @Transactional(readOnly = true)
    public Review findById(@Valid Id<Review, String> reviewId){
        return reviewRepository.findById(reviewId.value())
                .map(review -> {
                    review.setComments(reviewCommentRepository.findAllByReviewId(review.getId()));
                    return review;
                }).orElseThrow(() -> new NotFoundException(Review.class, reviewId));
    }



    @Transactional
    public Review payReserves(@Valid Id<Review, String> reviewId, @Positive int reserves){
        return reviewRepository.findById(reviewId.value())
                .map(review -> {
                    if(review.isReservesPaid()){
                        log.info("Reserves Already Paid: {}", reviewId);
                        return review;
                    }

                    Member member = review.getMember();
                    member.addReserves(reserves);
                    ReservesHistory newHistory = ReservesHistory.builder()
                            .amount(reserves)
                            .type(ReservesType.REVIEW)
                            .refId(reviewId.value())
                            .build();
                    member.addReservesHistory(newHistory);

                    review.reservesPaid();
                    return save(review);
                }).orElseThrow(() -> new NotFoundException(Review.class, reviewId));

    }

    private Review save(Review review){
        return reviewRepository.save(review);
    }
}
