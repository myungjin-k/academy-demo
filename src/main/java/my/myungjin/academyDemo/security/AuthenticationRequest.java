package my.myungjin.academyDemo.security;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthenticationRequest {

    private String principal;

    private String credentials;

}
