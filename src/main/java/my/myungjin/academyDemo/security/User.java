package my.myungjin.academyDemo.security;

import lombok.*;
import my.myungjin.academyDemo.domain.member.Role;

@EqualsAndHashCode(of = {"id", "role"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User{

    @Setter
    private String id;

    private String userId;

    private String password;

    private Role role;

    @Builder
    public User(String userId, String password, Role role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

}
