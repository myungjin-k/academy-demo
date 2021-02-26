package my.myungjin.academyDemo.domain.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import my.myungjin.academyDemo.domain.review.QReview;

public class ReviewPredicate {
    public static Predicate searchByIdAndWriterUserId(String id, String writerUserId){
        QReview review = QReview.review;
        BooleanBuilder builder = new BooleanBuilder();
        if(id != null){
            builder.and(review.id.eq(id));
        }
        if(writerUserId != null){
            builder.and(review.member.userId.eq(writerUserId));
        }
        return builder;
    }
}
