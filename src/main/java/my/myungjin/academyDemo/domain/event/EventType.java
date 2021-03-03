package my.myungjin.academyDemo.domain.event;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum EventType {

    DISCOUNT_RATIO("DR", "상품 할인(비율)"),
    DISCOUNT_AMOUNT("DA", "상품 할인(금액)"),
    COUPON("C", "쿠폰"),
    GIFT("G", "사은품 증정")
    ;

    private final String value;
    private final String description;

    public static EventType of(String value){
        for(EventType type : values()){
            if(value.equals(type.getValue())){
                return type;
            }
        }
        return null;
    }


}
