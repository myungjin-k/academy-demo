package my.myungjin.academyDemo.security;

import lombok.*;
import my.myungjin.academyDemo.domain.member.Role;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthenticationRequest {

    private String principal;

    private String credentials;

    private String role;

}
