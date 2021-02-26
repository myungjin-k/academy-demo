package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ReviewSearchRequest {

    private String reviewId;

    private String writerUserId;

    private String replyStatus;

}
