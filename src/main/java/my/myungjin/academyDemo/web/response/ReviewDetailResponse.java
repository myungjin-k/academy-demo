package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.domain.review.ReviewComment;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReviewDetailResponse {

    private String id;

    private int score;

    private String content;

    private String reviewImgUrl;

    private boolean isReservesPaid;

    private LocalDateTime createAt;

    private String writerUserId;

    private String writerId;

    private List<ReviewComment> comments;

    private String itemInfo;

    public ReviewDetailResponse(Review entity){
        this.id = entity.getId();
        this.content = entity.getContent();
        this.reviewImgUrl = entity.getReviewImg().orElse(null);
        this.score = entity.getScore();
        this.isReservesPaid = entity.isReservesPaid();
        this.createAt = entity.getCreateAt();
        Member writer = entity.getMember();
        this.writerId = writer.getId();
        this.writerUserId = writer.getUserId();
        this.itemInfo = StringUtils.join(entity.getItem().getItemDisplayName(),
                "[", entity.getOrderItem().getItemOption().getColor(), "/" , entity.getOrderItem().getItemOption().getSize(), "]");
        this.comments = entity.getComments();
    }
}
