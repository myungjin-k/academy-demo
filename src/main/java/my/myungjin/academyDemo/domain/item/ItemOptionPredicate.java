package my.myungjin.academyDemo.domain.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public class ItemOptionPredicate {
    public static Predicate search(String masterId, String color, String size){
        QItemMaster_ItemOption itemOption = QItemMaster_ItemOption.itemOption;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(itemOption.itemMaster.id.eq(masterId));
        if(color != null){
            builder.and(itemOption.color.eq(color));
        }
        if(size != null){
            builder.and(itemOption.size.eq(size));
        }
        return builder;
    }
}
