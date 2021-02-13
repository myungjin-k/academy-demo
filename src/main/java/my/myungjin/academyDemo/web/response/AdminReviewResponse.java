package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.review.Review;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Getter
public class AdminReviewResponse {

    private String id;

    private int score;

    private String content;

    private LocalDateTime createAt;

    private String itemId;

    private String itemInfo;

    private String writerUserId;

    public AdminReviewResponse (Review entity){
        this.id = entity.getId();
        this.content = entity.getContent();
        this.score = entity.getScore();
        this.createAt = entity.getCreateAt();
        this.itemId = entity.getItem().getId();
        this.itemInfo = StringUtils.join(entity.getItem().getItemDisplayName(),
                "[", entity.getOrderItem().getItemOption().getColor(), "/" , entity.getOrderItem().getItemOption().getSize(), "]");
        this.writerUserId = entity.getMember().getUserId();
    }

}
