package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.service.admin.MemberAdminService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.MemberSearchRequest;
import my.myungjin.academyDemo.web.request.PageRequest;
import my.myungjin.academyDemo.web.response.AdminMemberDetailResponse;
import my.myungjin.academyDemo.web.response.AdminMemberListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class MemberAdminController {

    private final MemberAdminService memberAdminService;


    @GetMapping("/member/search")
    @ApiOperation(value = "회원 검색")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<AdminMemberListResponse>> searchMembers(MemberSearchRequest request, PageRequest pageRequest){
        List<AdminMemberListResponse> result = memberAdminService.search(request.getMemberId(), request.getUserId())
                .stream()
                .map(AdminMemberListResponse::new)
                .collect(Collectors.toList());
        return OK(
                new PageImpl<>(
                        result,
                        pageRequest.of(),
                        result.size()
                )
        );
    }

    @GetMapping("/member/{id}")
    @ApiOperation(value = "회원 단건(상세) 조회")
    public Response<AdminMemberDetailResponse> getMemberDetail(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK") String id){
        return OK(
                new AdminMemberDetailResponse(memberAdminService.findByIdWithReservesHistory(Id.of(Member.class, id)))
        );
    }

    /*@PatchMapping("/member/{id}/payReserves")
    @ApiOperation(value = "적립금 수기 지급")
    public Response<Member> payAdminReserves(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK") String id, @RequestParam int minus, @RequestParam int plus){
        return OK(
                memberAdminService.updateReserves(Id.of(Member.class, id), minus, plus)
        );
    }*/

}
