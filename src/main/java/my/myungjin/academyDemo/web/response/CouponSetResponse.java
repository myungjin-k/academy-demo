package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.event.Coupon;
import my.myungjin.academyDemo.domain.event.Event;

import java.time.format.DateTimeFormatter;

@ToString
@Getter
public class CouponSetResponse {

    private String id;

    private String eventName;

    private int amount;

    private int minAmount;

    private int ratio;

    private char expiredYn;

    private char usedYn;

    private String endAt;

    private String startAt;

    private String couponType;

    public CouponSetResponse(Coupon entity) {
        this.id = entity.getId();
        this.expiredYn = entity.getExpiredYn();
        this.usedYn = entity.getUsedYn();
        Event eventMaster = entity.getEvent().getEvent();
        if(eventMaster.getAmount() > 0){
            this.amount = eventMaster.getAmount();
            this.minAmount = eventMaster.getMinPurchaseAmount();
            this.couponType = "AMOUNT";
        } else if(eventMaster.getRatio() > 0) {
            this.ratio = eventMaster.getRatio();
            this.couponType = "RATIO";
        }
        this.eventName = eventMaster.getName();
        this.endAt = eventMaster.getEndAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.startAt = eventMaster.getStartAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}
