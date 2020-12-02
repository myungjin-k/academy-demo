package my.myungjin.academyDemo.security;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.service.admin.AdminService;
import my.myungjin.academyDemo.service.member.MemberService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
        try {
            User user = User.builder()
                    .userId(token.getPrincipal())
                    .password(String.valueOf(token.getCredentials()))
                    .build();

            if(token.getType().name().equals("ADMIN"))
                user.setRole(adminService.login(token.getPrincipal(), String.valueOf(token.getCredentials())));
            else
                user.setRole(memberService.login(token.getPrincipal(), String.valueOf(token.getCredentials())));

            MyAuthenticationToken authenticated = new MyAuthenticationToken(user.getUserId(), user.getPassword(), createAuthorityList(user.getRole().name()));
            authenticated.setDetails(user);

            return authenticated;
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(MyAuthenticationToken.class, authentication);
    }
}
