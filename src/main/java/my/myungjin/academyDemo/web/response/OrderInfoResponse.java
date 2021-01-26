package my.myungjin.academyDemo.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.member.Member;

@ToString
@Getter
@AllArgsConstructor
public class OrderInfoResponse {

    private String name;

    private String tel;

    private String addr1;

    private String addr2;

    private int points;

    public static OrderInfoResponse of(Member entity){
        if(entity == null)
            return new OrderInfoResponse(null, null, null, null, 0);
        return new OrderInfoResponse(entity.getName(), entity.getTel(), entity.getAddr1(), entity.getAddr2(), entity.getReserves());
    }

}
