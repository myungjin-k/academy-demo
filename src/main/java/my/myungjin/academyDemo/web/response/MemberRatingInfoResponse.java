package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.Rating;

@Getter
public class MemberRatingInfoResponse {

    private String name;

    private String currRating;

    private int ratio;

    private String nextRating;

    private int orderedAmount;

    private int remainingAmount;

    private int reserves;

    public MemberRatingInfoResponse of(Member entity){
        this.name = entity.getName();
        Rating currRating = entity.getRating();
        this.currRating = currRating.getValue();
        this.ratio = (int) (currRating.getReserveRatio() * 100);
        this.orderedAmount = entity.getOrderAmount();
        Rating nextRating = Rating.of(currRating.getSeq() + 1);
        if(nextRating != null){
            this.nextRating = nextRating.getValue();
            this.remainingAmount = nextRating.getAmount() - this.orderedAmount;
        }
        this.reserves = entity.getReserves();
        return this;
    }
}
