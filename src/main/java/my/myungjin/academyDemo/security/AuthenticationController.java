package my.myungjin.academyDemo.security;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.error.UnauthorizedException;
import my.myungjin.academyDemo.web.Response;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final SessionRegistry mySessionRegistry;

    @PostMapping
    @ApiOperation(value = "회원 인증(api key 필요 없음)")
    public Response<User> auth(HttpServletRequest req,  @RequestBody AuthenticationRequest request) throws UnauthorizedException {
        try {
            MyAuthenticationToken token = new MyAuthenticationToken(request.getPrincipal(),
                    request.getCredentials(),
                    Role.of(request.getRole())
            );

            Authentication authentication = authenticationManager.authenticate(token);
            if(!mySessionRegistry.getAllSessions(authentication.getPrincipal(), false).isEmpty()){
                throw new UnauthorizedException("duplicated user login detected.");
            }
            mySessionRegistry.registerNewSession(req.getSession().getId(), authentication.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return OK((User) authentication.getDetails());
        } catch (AuthenticationException e){
            throw new UnauthorizedException(e.getMessage());
        }

    }
}
