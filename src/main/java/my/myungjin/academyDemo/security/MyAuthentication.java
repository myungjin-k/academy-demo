package my.myungjin.academyDemo.security;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import my.myungjin.academyDemo.domain.member.Role;

@EqualsAndHashCode(of = {"userId", "role"})
@ToString
@RequiredArgsConstructor
public class MyAuthentication {

    public final String id;

    public final String userId;

    public final Role role;


}
