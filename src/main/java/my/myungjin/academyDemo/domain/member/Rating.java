package my.myungjin.academyDemo.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rating {

    BRONZE("B", "BRONZE", 0, 0.01),
    SILVER("S", "SILVER", 100000, 0.02),
    GOLD("G", "GOLD", 500000, 0.03),
    VIP("V", "VIP", 1000000, 0.05);

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
}
