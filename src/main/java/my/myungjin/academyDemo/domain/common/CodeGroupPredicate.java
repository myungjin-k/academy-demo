package my.myungjin.academyDemo.domain.common;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class CodeGroupPredicate {
    public static Predicate search(String code, String nameEng, String nameKor){
        QCodeGroup codeGroup = QCodeGroup.codeGroup;
        BooleanBuilder builder = new BooleanBuilder();
        if(code != null){
            builder.and(codeGroup.code.containsIgnoreCase(code));
        }
        if(nameEng != null){
            builder.and(codeGroup.nameEng.containsIgnoreCase(nameEng));
        }
        if(nameKor != null){
            builder.and(codeGroup.nameKor.containsIgnoreCase(nameKor));
        }
        return builder;
    }
}
