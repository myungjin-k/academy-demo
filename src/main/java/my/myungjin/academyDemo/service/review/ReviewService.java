package my.myungjin.academyDemo.service.review;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.aws.S3Client;
import my.myungjin.academyDemo.commons.AttachedFile;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.*;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.domain.review.ReviewCommentRepository;
import my.myungjin.academyDemo.domain.review.ReviewRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.error.StatusNotSatisfiedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ReviewRepository reviewRepository;

    private final OrderItemRepository orderItemRepository;

    private final DeliveryItemRepository deliveryItemRepository;

    private final ReviewCommentRepository reviewCommentRepository;

    private final S3Client s3Client;

    private final String S3_BASE_PATH = "review";


    @Transactional
    public List<Review> findAllDesc(){
        return reviewRepository.findAllByOrderByCreateAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Review> findAllByItem(@Valid Id<ItemDisplay, String> itemId, Pageable pageable){
        return reviewRepository.findByItemId(itemId.value())
                .stream()
                .peek(review -> review.setComments(reviewCommentRepository.findAllByReviewId(review.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Review findById(@Valid Id<Review, String> reviewId){
        return reviewRepository.findById(reviewId.value())
                .orElseThrow(() -> new NotFoundException(Review.class, reviewId));
    }


    @Transactional(readOnly = true)
    public Review findByMemberAndItem(@Valid Id<Member, String> memberId, @Valid Id<OrderItem, String> itemId){
        //  TODO Review 엔티티 중복 조인(?) 문제
        return reviewRepository.findByOrderItemIdAndMemberId(itemId.value(), memberId.value())
                .orElseThrow(() -> new NotFoundException(Review.class, memberId, itemId));
    }

    private Optional<String> uploadReviewImage(AttachedFile reviewImageFile) {
        String reviewImageUrl = null;
        if (reviewImageFile != null) {
            String key = reviewImageFile.randomName(S3_BASE_PATH, "jpeg");
            try {
                reviewImageUrl = s3Client.upload(reviewImageFile.inputStream(),
                        reviewImageFile.length(),
                        key,
                        reviewImageFile.getContentType(),
                        null);
            } catch (AmazonS3Exception e) {
                log.warn("Amazon S3 error (key: {}): {}", key, e.getMessage(), e);
            }
        }
        return ofNullable(reviewImageUrl);
    }

    private void deleteReviewImage(String reviewImage){
        try{
            s3Client.delete(reviewImage, S3_BASE_PATH);
        } catch (AmazonS3Exception e){
            log.warn("Amazon S3 error (key: {}): {}", reviewImage, e.getMessage(), e);
        }
    }

    @Transactional
    public Review write(@Valid Id<Member, String> memberId, @Valid Id<Member, String> loginUserId,
                        @Valid Id<OrderItem, String> orderItemId, @Valid Review review, AttachedFile reviewImgFile){
        if(!loginUserId.value().equals(memberId.value()))
            throw new IllegalArgumentException("member id must be equal to login user id(member="+ memberId+", loginUser=" +loginUserId +")");

        OrderItem orderItem = orderItemRepository.findById(orderItemId.value())
                .orElseThrow(() -> new NotFoundException(OrderItem.class, orderItemId));
        List<DeliveryItem> deliveryItems = deliveryItemRepository
                .findAllByDeliveryOrderAndItemOptionIdAndDeliveryStatusNotOrderByCreateAtDesc(orderItem.getOrder(), orderItem.getItemOption().getId(), DeliveryStatus.DELETED);
        if(deliveryItems.isEmpty())
            throw new NotFoundException(DeliveryItem.class, orderItem);
        DeliveryStatus deliveryStatus = deliveryItems.get(0).getDelivery().getStatus();
        if(!deliveryStatus.equals(DeliveryStatus.DELIVERED))
            throw new StatusNotSatisfiedException(Review.class, orderItemId, deliveryStatus);

        Review found = reviewRepository.findByOrderItemIdAndMemberId(orderItemId.value(), memberId.value()).orElse(null);
        if(found != null){
            log.warn("Review Already Exists: {}", found);
            return found;
        }

        review.setOrderItem(orderItem);
        review.setItem(orderItem.getItemOption().getItemDisplay());
        review.setMember(orderItem.getOrder().getMember());
        review.setReviewImg(uploadReviewImage(reviewImgFile).orElse(null));
        return save(review);
    }

    @Transactional
    public Review modify(@Valid Id<Member, String> memberId, @Valid Id<Member, String> loginUserId,
                         @Valid Id<Review, String> reviewId, @NotBlank String content, @Positive int score, AttachedFile reviewImgFile){
        if(!loginUserId.value().equals(memberId.value()))
            throw new IllegalArgumentException("member id must be equal to login user id(member="+ memberId+", loginUser=" +loginUserId +")");

        Review review = reviewRepository.findByMemberIdAndId(memberId.value(), reviewId.value())
                .orElseThrow(() -> new NotFoundException(Review.class, memberId, reviewId));
        String updatedImg = uploadReviewImage(reviewImgFile).orElse(null);
        if(updatedImg != null){
            if(review.getReviewImg() != null){
                deleteReviewImage(review.getReviewImg());
            }
            review.setReviewImg(updatedImg);
        }
        review.modify(content, score);
        return save(review);
    }

    private Review save(Review review){
        return reviewRepository.save(review);
    }

}
