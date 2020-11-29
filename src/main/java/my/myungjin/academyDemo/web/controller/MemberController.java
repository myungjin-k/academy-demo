package my.myungjin.academyDemo.web.controller;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.service.member.MemberService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.MemberRequest;
import my.myungjin.academyDemo.web.request.PwChangeRequest;
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
        // TODO Front page
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



}
