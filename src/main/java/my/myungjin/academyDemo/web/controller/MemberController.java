package my.myungjin.academyDemo.web.controller;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.service.member.MemberService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.MemberRequest;
import my.myungjin.academyDemo.web.request.PwChangeRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Response<Member> join(@RequestBody MemberRequest request){
        return OK(
                memberService.join(request.newMember())
        );
    }

    @GetMapping("/id")
    public Response<String> forgotUserId(@RequestParam Map<String, String> params){
        return OK(
                memberService.findUserId(params.get("tel"))
                        .orElse("")
        );
    }

    @GetMapping("/password")
    public Response<String> forgotUserPwd(@RequestParam Map<String, String> paramMap){
        return OK(
                memberService.findPassword(paramMap.get("email"))
                        .orElse("")
        );
    }

    @PatchMapping("/password")
    public Response<Member> changePassword(@RequestBody PwChangeRequest request){
        return OK(
          memberService.modifyPassword(request.getId(), request.getNewPassword())
        );
    }

    @GetMapping("/me")
    public Response<Member> getMyInfo(@AuthenticationPrincipal Authentication authentication){
        return OK(
                memberService.findMyInfo(Id.of(Member.class, authentication.getName()))
        );
    }

    @PutMapping("/me")
    public Response<Member> changePassword(@AuthenticationPrincipal Authentication authentication,
                                           @RequestBody MemberRequest request){
        Id<Member, String> id = Id.of(Member.class, authentication.getName());
        return OK(
                memberService.modify(id, request.toMember(id))
        );
    }

}
