package my.myungjin.academyDemo.security;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.service.member.MemberService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.util.ClassUtils.isAssignable;

@RequiredArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final MemberService memberService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyAuthenticationToken token = (MyAuthenticationToken) authentication;
        return processUserAuthentication(token);
    }

    private Authentication processUserAuthentication(MyAuthenticationToken token){
        Member member = memberService.login(token.getPrincipal(), String.valueOf(token.getCredentials()));
        // TODO Role
        MyAuthenticationToken authenticated = new MyAuthenticationToken(member.getUserId(), member.getPassword(), createAuthorityList(Role.USER.getValue()));
        authenticated.setDetails(member);
        // TODO Exception
        return authenticated;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(MyAuthenticationToken.class, authentication);
    }
}
