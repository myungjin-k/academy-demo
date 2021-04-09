package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Admin;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.qna.Qna;
import my.myungjin.academyDemo.domain.qna.QnaReply;
import my.myungjin.academyDemo.security.User;
import my.myungjin.academyDemo.service.qna.QnaService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.PageRequest;
import my.myungjin.academyDemo.web.request.QnaRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;
import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class QnaController {

    private final QnaService qnaService;

    @GetMapping("/mall/member/{id}/qna/list")
    @ApiOperation(value = "회원별 문의 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<Qna>> findMyQnas(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String id,
            @AuthenticationPrincipal Authentication authentication,
            PageRequest pageRequest){
        List<Qna> results = new ArrayList<>(qnaService.findByMember(
                Id.of(Member.class, ((User) authentication.getDetails()).getId()),
                Id.of(Member.class, id)
        ));
        return OK(new PageImpl<>(results, pageRequest.of(), results.size()));
    }

    @PostMapping( "/mall/member/{id}/qna")
    @ApiOperation(value = "회원별 문의 작성")
    public Response<Qna> ask(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String id,
            @ModelAttribute QnaRequest qnaRequest,
            @AuthenticationPrincipal Authentication authentication,
            @RequestPart(required = false) MultipartFile attachedImageFile
            ) throws IOException {
        return OK(
                qnaService.ask(
                        Id.of(Member.class, ((User) authentication.getDetails()).getId()),
                        Id.of(Member.class, id),
                        qnaRequest.getCateId(),
                        qnaRequest.getItemId(),
                        qnaRequest.newQna(),
                        toAttachedFile(attachedImageFile)
                )
        );
    }

    @PutMapping("/mall/member/{memberId}/qna/{qnaId}")
    @ApiOperation(value = "회원별 문의 수정")
    public Response<Qna> modify(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @PathVariable @ApiParam(value = "조회 대상 문의 PK", example = "1") Long qnaId,
            @ModelAttribute QnaRequest qnaRequest,
            @AuthenticationPrincipal Authentication authentication,
            @RequestPart(required = false) MultipartFile attachedImageFile
    ) throws IOException {
        return OK(
                qnaService.modify(
                        Id.of(Member.class, ((User) authentication.getDetails()).getId()),
                        Id.of(Member.class, memberId),
                        Id.of(Qna.class, qnaId),
                        qnaRequest.getCateId(),
                        qnaRequest.toEntity(qnaId),
                        toAttachedFile(attachedImageFile)
                )
        );
    }

    @DeleteMapping("/mall/member/{memberId}/qna/{qnaId}")
    @ApiOperation(value = "회원별 문의 삭제")
    public Response<Qna> delete(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @PathVariable @ApiParam(value = "조회 대상 문의 PK", example = "1") Long qnaId,
            @AuthenticationPrincipal Authentication authentication
    ){
        return OK(
                qnaService.delete(
                        Id.of(Member.class, ((User) authentication.getDetails()).getId()),
                        Id.of(Member.class, memberId),
                        Id.of(Qna.class, qnaId)
                )
        );
    }

    @PostMapping( "/admin/qna/{id}/reply")
    @ApiOperation(value = "관리자 문의 답변 작성")
    public Response<QnaReply> reply(
            @PathVariable @ApiParam(value = "조회 대상 문의 PK", example = "1") Long id,
            @ModelAttribute QnaRequest qnaRequest,
            @RequestPart(required = false) MultipartFile attachedImageFile,
            @AuthenticationPrincipal Authentication authentication
    ) throws IOException {
        return OK(
                qnaService.reply(
                        Id.of(Qna.class, id),
                        Id.of(Admin.class, ((User) authentication.getDetails()).getId()),
                        qnaRequest.newReply()
                )
        );
    }
}
