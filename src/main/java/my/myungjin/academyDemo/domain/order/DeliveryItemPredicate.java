package my.myungjin.academyDemo.domain.order;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import my.myungjin.academyDemo.commons.Id;

import java.time.LocalDate;

public class DeliveryItemPredicate {
    public static Predicate search(Id<Order, String> orderId, LocalDate start, LocalDate end){
        QDeliveryItem item = QDeliveryItem.deliveryItem;
        BooleanBuilder builder = new BooleanBuilder();
        if(orderId != null){
            builder.and(item.orderItem.order.id.eq(orderId.value()));
        }
        if(start != null){
            builder.and(item.createAt.after(start.atStartOfDay()));
        }
        if(end != null){
            LocalDate endPlus1 = end.plusDays(1);
            builder.and(item.createAt.before(endPlus1.atStartOfDay()));
        }
        return builder;
    }
}
