package my.myungjin.academyDemo.domain.review;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class ReviewPredicate {
    public static Predicate searchByIdAndWriterUserId(String id, String writerUserId, String replyStatus){
        QReview review = QReview.review;
        BooleanBuilder builder = new BooleanBuilder();
        if(id != null && !id.isBlank()){
            builder.and(review.id.eq(id));
        }
        if(writerUserId != null && !writerUserId.isBlank()){
            builder.and(review.member.userId.eq(writerUserId));
        }
        if(replyStatus != null && !replyStatus.isBlank()){
            if("REPLIED".equals(replyStatus))
                builder.and(review.comments.size().gt(0));
            else if("UNREPLIED".equals(replyStatus))
                builder.and(review.comments.size().eq(0));
        }
        return builder;
    }
}
