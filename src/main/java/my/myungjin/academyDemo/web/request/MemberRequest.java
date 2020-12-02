package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.util.Util;

@AllArgsConstructor
@ToString
@Getter
public class MemberRequest {

    private String userId;

    private String password;

    private String name;

    private String email;

    private String tel;

    private String addr1;

    private String addr2;

    public Member newMember(){
        return Member.builder()
                .id(Util.getUUID())
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .tel(tel)
                .addr1(addr1)
                .addr2(addr2)
                .build();
    }
    public Member toMember(Id<Member, String> id){
        return Member.builder()
                .id(id.value())
                .password(password)
                .name(name)
                .email(email)
                .tel(tel)
                .addr1(addr1)
                .addr2(addr2)
                .build();
    }
}
