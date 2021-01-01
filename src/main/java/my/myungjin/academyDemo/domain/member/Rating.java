package my.myungjin.academyDemo.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rating {

    BRONZE(1, "B", "BRONZE", 0, 0.01),
    SILVER(2, "S", "SILVER", 100000, 0.02),
    GOLD(3, "G", "GOLD", 500000, 0.03),
    VIP(4, "V", "VIP", 1000000, 0.05);

    private final int seq;
    private final String code;
    private final String value;
    private final int amount;
    private final double reserveRatio;

    public static Rating of(String code){
        for(Rating rating : Rating.values()){
            if(rating.getCode().equalsIgnoreCase(code)){
                return rating;
            }
        }
        return null;
    }

    public static Rating of(int seq){
        for(Rating rating : Rating.values()){
            if(rating.getSeq() == seq){
                return rating;
            }
        }
        return null;
    }

}
