package my.myungjin.academyDemo.domain.member;

import lombok.Getter;

@Getter
public class User {
    private String userId;
    private String password;
    private Role role;
    public void setRole(Role role){
        this.role = role;
    }
}
