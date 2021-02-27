package my.myungjin.academyDemo.web.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.member.Member;

@ToString
@Getter
public class MemberInfoResponse {

    private String id;

    private String userId;

    private String name;

    @JsonIgnore
    private String password;

    private String tel;

    private String addr1;

    private String addr2;

    public MemberInfoResponse(Member entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.name = entity.getName();
        this.password = entity.getPassword();
        this.tel = entity.getTel();
        this.addr1 = entity.getAddr1();
        this.addr2 = entity.getAddr2();
    }
}
