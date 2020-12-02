package my.myungjin.academyDemo.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
public class User {

    private String userId;

    private String password;

    private Role role;

    @Builder
    public User(String userId, String password, Role role) {
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

    public void role(Role role){
        this.role = role;
    }
}
