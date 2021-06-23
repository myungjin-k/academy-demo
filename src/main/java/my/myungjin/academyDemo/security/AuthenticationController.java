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
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final SessionRegistry mySessionRegistry;

    @PostMapping
    @ApiOperation(value = "회원 인증(api key 필요 없음)")
    public Response<AuthenticaionResult> auth(HttpServletRequest req, HttpServletResponse res, @RequestBody AuthenticationRequest request) throws UnauthorizedException {
        try {
            AuthenticaionResult authenticaionResult = new AuthenticaionResult();
            MyAuthenticationToken token = new MyAuthenticationToken(request.getPrincipal(),
                    request.getCredentials(),
                    Role.of(request.getRole())
            );

            Authentication authentication = authenticationManager.authenticate(token);
            List<SessionInformation> sessionInfos = mySessionRegistry.getAllSessions(authentication.getPrincipal(), false);
            if(!sessionInfos.isEmpty()){
                sessionInfos.forEach(SessionInformation::expireNow);
                authenticaionResult.setMessage("duplicate login session detected. expire last sessions");
            }
            req.getSession(true);
            String sessionId = req.changeSessionId();
            mySessionRegistry.registerNewSession(sessionId, authentication.getPrincipal());
            Cookie cookie = new Cookie("JSESSIONID", sessionId);
            cookie.setHttpOnly(true);
            res.addCookie(cookie);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            authenticaionResult.setUser((User) authentication.getDetails());
            return OK(authenticaionResult);
        } catch (AuthenticationException e){
            throw new UnauthorizedException(e.getMessage());
        }

    }
}
