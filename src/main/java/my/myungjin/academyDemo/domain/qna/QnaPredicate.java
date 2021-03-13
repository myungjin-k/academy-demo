package my.myungjin.academyDemo.domain.qna;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class QnaPredicate {

    public static Predicate searchByCategoryAndItem(String cateId, String itemId){
        QQna qna = QQna.qna;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qna.category.id.eq(cateId));
        if(itemId != null){
            builder.and(qna.item.id.eq(itemId));
        }
        return builder;
    }

}
