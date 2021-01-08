package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.security.User;
import my.myungjin.academyDemo.service.member.MemberService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.MemberRequest;
import my.myungjin.academyDemo.web.request.PwChangeRequest;
import my.myungjin.academyDemo.web.response.MemberInformRatingResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api/mall/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ApiOperation(value = "회원 가입(api 키 필요 없음)")
    public Response<Member> join(@RequestBody MemberRequest request){
        return OK(
                memberService.join(request.newMember())
        );
    }

    @GetMapping("/id")
    @ApiOperation(value = "회원 아이디 찾기(api 키 필요 없음)")
    public Response<String> forgotUserId(@RequestParam @ApiParam(value = "조회 대상 회원 정보(전화번호)", defaultValue = "010-1234-5678") String tel){
        return OK(
                memberService.findUserId(tel)
                        .orElse("")
        );
    }

    @GetMapping("/password")
    @ApiOperation(value = "회원 비밀번호 찾기(api 키 필요 없음)")
    public Response<String> forgotUserPwd(@RequestParam @ApiParam(value = "조회 대상 회원 정보(이메일)", defaultValue = "open7894.v2@gmail.com") String email){
        return OK(
                memberService.findPassword(email)
                        .orElse("")
        );
    }

    @PatchMapping("/password")
    @ApiOperation(value = "회원 비밀번호 변경")
    public Response<Member> changePassword(@RequestBody PwChangeRequest request){
        return OK(
          memberService.modifyPassword(request.getId(), request.getNewPassword())
        );
    }

    @GetMapping("/me")
    @ApiOperation(value = "회원 정보 조회")
    public Response<Member> getMyInfo(@AuthenticationPrincipal User authentication){
        return OK(
                memberService.findMyInfo(Id.of(Member.class, authentication.getId()))
        );
    }

    @PutMapping("/me")
    @ApiOperation(value = "회원 정보 수정")
    public Response<Member> modifyMyInfo(@AuthenticationPrincipal User authentication,
                                           @RequestBody MemberRequest request){
        Id<Member, String> id = Id.of(Member.class, authentication.getId());
        return OK(
                memberService.modify(id, request.toMember(id))
        );
    }
}
