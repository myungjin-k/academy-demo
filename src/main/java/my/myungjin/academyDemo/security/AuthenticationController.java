package my.myungjin.academyDemo.security;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.security.AuthenticationRequest;
import my.myungjin.academyDemo.security.MyAuthenticationToken;
import my.myungjin.academyDemo.web.Response;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/auth")
@Controller
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    @PostMapping
    public Response<Authentication> auth(@RequestBody AuthenticationRequest request) {
        MyAuthenticationToken token = new MyAuthenticationToken(request.getPrincipal(), request.getCredentials(), Role.of(request.getRole()));

        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(token);
        return OK(authentication);
    }
}
