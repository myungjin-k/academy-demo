package my.myungjin.academyDemo.domain.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import java.time.LocalDate;

public class ItemDisplayPredicate {
    public static Predicate searchByNameAndDate(String name, LocalDate start, LocalDate end, boolean isFromMall){
        QItemDisplay itemDisplay = QItemDisplay.itemDisplay;
        BooleanBuilder builder = new BooleanBuilder();
        if(name != null){
            builder.and(itemDisplay.itemDisplayName.containsIgnoreCase(name));
        }
        if(start != null){
            builder.and(itemDisplay.createAt.after(start.atStartOfDay()));
        }
        if(end != null){
            LocalDate endPlus1 = end.plusDays(1);
            builder.and(itemDisplay.createAt.before(endPlus1.atStartOfDay()));
        }
        if(isFromMall){
            builder.and(itemDisplay.status.eq(ItemStatus.ON_SALE));
        }
        return builder;
    }
}
