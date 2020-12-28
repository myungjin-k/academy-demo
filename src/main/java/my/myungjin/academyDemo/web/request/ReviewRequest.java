package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.util.Util;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class ReviewRequest {

    private int score;

    private String content;

    public Review newReview(){
        return Review.builder()
                .id(Util.getUUID())
                .score(score)
                .content(content)
                .build();
    }
}
