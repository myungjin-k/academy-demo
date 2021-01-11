package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.review.Review;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class ReviewRequest {

    private int score;

    private String content;

    public Review newReview(){
        return Review.builder()
                .score(score)
                .content(content)
                .build();
    }
}
