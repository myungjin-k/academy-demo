package my.myungjin.academyDemo.domain.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import my.myungjin.academyDemo.domain.member.Role;

@ToString
@Getter
@RequiredArgsConstructor
public enum ItemStatus {
    READY_TO_SALE(0, "판매대기중"),
    ON_SALE(1, "판매중"),
    SOLD_OUT(2, "품절"),
    SALE_END(3, "판매종료");

    private final int value;
    private final String description;

    public static ItemStatus of(int value){
        for(ItemStatus status : ItemStatus.values()){
            if(status.getValue() == value)
                return status;
        }
        return null;
    }
}
