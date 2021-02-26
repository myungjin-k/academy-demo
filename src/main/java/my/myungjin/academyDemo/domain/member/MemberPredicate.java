package my.myungjin.academyDemo.domain.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class MemberPredicate {
    public static Predicate search(String memberId, String userId){
        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();
        if(memberId != null && !memberId.isBlank()){
            builder.and(member.id.eq(memberId));
        }
        if(userId != null && !userId.isBlank()){
            builder.and(member.userId.containsIgnoreCase(userId));
        }
        return builder;
    }

}
