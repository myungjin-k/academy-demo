package my.myungjin.academyDemo.web.controller;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.security.AuthenticationRequest;
import my.myungjin.academyDemo.security.MyAuthenticationToken;
import my.myungjin.academyDemo.service.member.MemberService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.MemberRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;

    @PostMapping
    public Response<Member> join(MemberRequest request){
        // TODO Front page
        return OK(
                memberService.join(request.newMember())
        );
    }

    @PostMapping("/auth")
    public Response<Authentication> auth(@RequestBody AuthenticationRequest request) {
        MyAuthenticationToken token = new MyAuthenticationToken(request.getPrincipal(), request.getCredentials());

        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(token);
        return OK(authentication);
    }
}
