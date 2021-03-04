package my.myungjin.academyDemo.domain.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class EventPredicate {
    public static Predicate searchByStatus(String status){
        QEvent event = QEvent.event;
        BooleanBuilder builder = new BooleanBuilder();
        if(status != null && !status.isBlank()){
            builder.and(event.status.eq(EventStatus.valueOf(status)));
        }
        return builder;
    }
}
