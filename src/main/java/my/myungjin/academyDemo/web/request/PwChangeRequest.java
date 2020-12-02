package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;

@ToString
@Getter
@AllArgsConstructor
public class PwChangeRequest {

    private Id<Member, String> id;

    private String newPassword;

}
