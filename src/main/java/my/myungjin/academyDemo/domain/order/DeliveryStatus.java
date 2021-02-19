package my.myungjin.academyDemo.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    REQUESTED(1, "결제완료"),
    CHECKED(2, "배송준비중"),
    SHIPPED(3, "배송중"),
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
