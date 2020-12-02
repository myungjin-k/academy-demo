package my.myungjin.academyDemo.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER");

    private final String value;

    public static Role of(String name){
        for(Role role : Role.values()){
            if(role.name().equalsIgnoreCase(name))
                return role;
        }
        return null;
    }
}
