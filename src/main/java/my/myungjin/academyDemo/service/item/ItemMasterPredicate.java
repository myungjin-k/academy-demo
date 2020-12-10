package my.myungjin.academyDemo.service.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import my.myungjin.academyDemo.domain.item.QItemMaster;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ItemMasterPredicate {
    public static Predicate search(String itemName, LocalDate start, LocalDate end){
        QItemMaster itemMaster = QItemMaster.itemMaster;
        BooleanBuilder builder = new BooleanBuilder();
        if(itemName != null){
            builder.and(itemMaster.itemName.containsIgnoreCase(itemName));
        }
        if(start != null){
            builder.and(itemMaster.createAt.after(LocalDateTime.from(start)));
        }
        if(end != null){
            builder.and(itemMaster.createAt.before(LocalDateTime.from(end)));
        }
        return builder;
    }
}
