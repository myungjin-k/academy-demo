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
import org.springframework.web.bind.annotation.*;

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public Response<Member> join(@RequestBody MemberRequest request){
        // TODO Front page
        return OK(
                memberService.join(request.newMember())
        );
    }

}
