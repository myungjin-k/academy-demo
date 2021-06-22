package my.myungjin.academyDemo.security;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Role;
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

    private Authentication processUserAuthentication(MyAuthenticationToken requested){
        try {
            User user = User.builder()
                    .userId(requested.getPrincipal().toString())
                    .password(String.valueOf(requested.getCredentials()))
                    .role(requested.getType())
                    .build();

            if(user.getRole().equals(Role.ADMIN)){
                user.setId(adminService.login(user.getUserId(), user.getPassword()));;
            } else {
                user.setId(memberService.login(user.getUserId(), user.getPassword()));
            }

            MyAuthenticationToken authenticated =
                    new MyAuthenticationToken(new MyAuthentication(user.getId(), user.getUserId(), user.getRole()),
                            null,
                            createAuthorityList(user.getRole().getValue()));
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
