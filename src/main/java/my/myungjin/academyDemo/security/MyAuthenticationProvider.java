package my.myungjin.academyDemo.security;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Admin;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.domain.member.User;
import my.myungjin.academyDemo.service.admin.AdminService;
import my.myungjin.academyDemo.service.member.MemberService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.util.ClassUtils.isAssignable;

@RequiredArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final MemberService memberService;

    private final AdminService adminService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyAuthenticationToken token = (MyAuthenticationToken) authentication;
        return processUserAuthentication(token);
    }

    private Authentication processUserAuthentication(MyAuthenticationToken token){
        // TODO Admin
        User user = new User();

        if(token.getType().name().equals("ADMIN"))
                user =  adminService.login(token.getPrincipal(), String.valueOf(token.getCredentials()));
        else
                user = memberService.login(token.getPrincipal(), String.valueOf(token.getCredentials()));

        // TODO Role
        MyAuthenticationToken authenticated = new MyAuthenticationToken(user.getUserId(), user.getPassword(), createAuthorityList(user.getRole().name()));
        authenticated.setDetails(user);
        // TODO Exception
        return authenticated;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(MyAuthenticationToken.class, authentication);
    }
}
