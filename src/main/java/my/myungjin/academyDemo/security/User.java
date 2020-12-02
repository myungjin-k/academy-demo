package my.myungjin.academyDemo.security;

import lombok.*;
import my.myungjin.academyDemo.domain.member.Role;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    private String userId;

    private String password;

    @Setter
    private Role role;

    @Builder
    public User(String userId, String password, Role role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

}
