package my.myungjin.academyDemo.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rating {

    BRONZE("B", "BRONZE", 0),
    SILVER("S", "SILVER", 100000),
    GOLD("G", "GOLD", 500000),
    VIP("V", "VIP", 1000000);

    private final String code;
    private final String value;
    private final int amount;

    public static Rating of(String code){
        for(Rating rating : Rating.values()){
            if(rating.getCode().equalsIgnoreCase(code)){
                return rating;
            }
        }
        return null;
    }
}
