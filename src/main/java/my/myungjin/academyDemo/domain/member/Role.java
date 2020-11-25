package my.myungjin.academyDemo.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;

    public static Role of(String name){
        for(Role role : Role.values()){
            if(role.value.equalsIgnoreCase(name))
                return role;
        }
        return null;
    }
}
