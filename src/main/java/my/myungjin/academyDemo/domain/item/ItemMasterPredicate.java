package my.myungjin.academyDemo.domain.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import java.time.LocalDate;

public class ItemMasterPredicate {
    public static Predicate search(String itemName, LocalDate start, LocalDate end){
        QItemMaster itemMaster = QItemMaster.itemMaster;
        BooleanBuilder builder = new BooleanBuilder();
        if(itemName != null){
            builder.and(itemMaster.itemName.containsIgnoreCase(itemName));
        }
        if(start != null){
            builder.and(itemMaster.createAt.after(start.atStartOfDay()));
        }
        if(end != null){
            LocalDate endPlus1 = end.plusDays(1);
            builder.and(itemMaster.createAt.before(endPlus1.atStartOfDay()));
        }
        return builder;
    }
}
