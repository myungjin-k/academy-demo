package my.myungjin.academyDemo.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    PROCESSING(1, "배송준비중"),
    SHIPPED(2, "발송완료"),
    DELIVERING(3, "배송중"),
    DELIVERED(4, "배송완료"),
    DELETED(9, "배송취소")
    ;

    private final int value;
    private final String description;

    public static DeliveryStatus of(int value){
        for(DeliveryStatus status : DeliveryStatus.values()){
            if(status.getValue() == value)
                return status;
        }
        return null;
    }
}
