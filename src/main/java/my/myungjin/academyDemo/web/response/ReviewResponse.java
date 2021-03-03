package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayOption;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.domain.review.ReviewComment;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
public class ReviewResponse {

    private String id;

    private String content;

    private int score;

    private String writerUserId;

    private String reviewImgUrl;

    private String optionInfo;

    private List<ReviewComment> comments;

    public ReviewResponse of(Review entity){
        this.content = entity.getContent();
        this.score = entity.getScore();
        this.id = entity.getId();
        String userId = entity.getMember().getUserId();
        this.writerUserId = StringUtils.rightPad(StringUtils.substring(userId, 0, 2), userId.length(), '*');
        this.reviewImgUrl = entity.getReviewImg();
        ItemDisplayOption option = entity.getOrderItem().getItemOption();
        this.optionInfo = StringUtils.join(option.getColor(), "/", option.getSize());
        this.comments = entity.getComments();
        return this;
    }

}
