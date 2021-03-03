package my.myungjin.academyDemo.domain.event;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum EventStatus {

    ON(1, "활성화"),
    OFF(0, "비활성화")
    ;

    private final int value;
    private final String description;

    public static EventStatus of(int value){
        for(EventStatus type : values()){
            if(value == type.value){
                return type;
            }
        }
        return null;
    }


}
