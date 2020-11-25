package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.ToString;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.Rating;
import my.myungjin.academyDemo.util.Util;

@AllArgsConstructor
@ToString
public class MemberRequest {
    private String userId;
    private String password;
    private String name;
    private String tel;
    private String addr1;
    private String addr2;

    public Member newMember(){
        return Member.builder()
                .id(Util.getUUID())
                .userId(userId)
                .password(password)
                .name(name)
                .tel(tel)
                .addr1(addr1)
                .addr2(addr2)
                .rating(Rating.BRONZE)
                .build();
    }
}
