package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.service.admin.CommonCodeService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.CodeGroupRequest;
import my.myungjin.academyDemo.web.request.CommonCodeRequest;
import my.myungjin.academyDemo.web.request.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RestController
public class CommonController {

    private final CommonCodeService sampleService;

    @GetMapping("/codeGroup/list")
    @ApiOperation(value = "코드 그룹 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<CodeGroup>> codeGroups(PageRequest pageRequest){
        return OK(sampleService.findAllGroups(pageRequest.of()));
    }

    @PostMapping("/codeGroup")
    @ApiOperation(value = "코드 그룹 등록")
    public Response<CodeGroup> registGroup(@RequestBody CodeGroupRequest request){
        return OK(sampleService.registGroup(request.newCodeGroup()));

    }

    @PutMapping("/codeGroup/{id}")
    @ApiOperation(value = "코드 그룹 수정")
    public Response<CodeGroup> modifyCodeGroup(@PathVariable @ApiParam(value = "대상 코드그룹 PK", defaultValue = "246fa96f9b634a56aaac5884de186ebc") String id,
                                               @RequestBody CodeGroupRequest request){
        return OK(
                sampleService.modifyGroup(Id.of(CodeGroup.class, id),
                        request.getCode(),
                        request.getNameEng(),
                        request.getNameKor())
        );
    }

    @DeleteMapping("/codeGroup/{id}")
    @ApiOperation(value = "코드 그룹 삭제")
    public void removeCodeGroup(@PathVariable @ApiParam(value = "대상 코드그룹 PK") String id){
        sampleService.removeGroup(Id.of(CodeGroup.class, id));
    }

    @GetMapping("/codeGroup/{id}/commonCode/list")
    @ApiOperation(value = "공통 코드 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<CommonCode>> commonCodesByGroupIdWithPage(
            @PathVariable @ApiParam(value = "대상 코드그룹 PK", defaultValue = "246fa96f9b634a56aaac5884de186ebc") String id,
            PageRequest pageRequest){
        return OK(sampleService.findAllCommonCodeByGroupIdWithPage(Id.of(CodeGroup.class, id), pageRequest.of()));
    }

    @PostMapping("/codeGroup/{id}/commonCode")
    @ApiOperation(value = "공통 코드 등록")
    public Response<CommonCode> newCommonCode(@PathVariable @ApiParam(value = "대상 코드그룹 PK", defaultValue = "246fa96f9b634a56aaac5884de186ebc") String id,
                                              @RequestBody CommonCodeRequest request){
        return OK(
                sampleService.registCommonCode(Id.of(CodeGroup.class, id),
                        request.newCommonCode(Id.of(CodeGroup.class, id)))
        );
    }

    @PutMapping("/codeGroup/{id}/commonCode/{codeId}")
    @ApiOperation(value = "공통 코드 수정")
    public Response<CommonCode> modifyCode(
            @PathVariable @ApiParam(value = "대상 코드그룹 PK", defaultValue = "246fa96f9b634a56aaac5884de186ebc") String id,
            @PathVariable @ApiParam(value = "대상 공통코드 PK", defaultValue = "36f651a982274a5b95dac3e9d85b0d1a") String codeId,
            @RequestBody CommonCodeRequest request){
        return OK(
                sampleService.modifyCode(
                        Id.of(CodeGroup.class, id),
                        Id.of(CommonCode.class, codeId),
                        request.getCode(),
                        request.getNameEng(),
                        request.getNameKor())
        );
    }

    @DeleteMapping("/codeGroup/{id}/commonCode/{codeId}")
    @ApiOperation(value = "공통 코드 삭제")
    public void removeCode(
            @PathVariable @ApiParam(value = "대상 코드그룹 PK", defaultValue = "246fa96f9b634a56aaac5884de186ebc") String id,
            @PathVariable @ApiParam(value = "대상 공통코드 PK", defaultValue = "36f651a982274a5b95dac3e9d85b0d1a") String codeId){
        sampleService.removeCode(Id.of(CodeGroup.class, id), Id.of(CommonCode.class, codeId));
    }

    @GetMapping("/commonCode/search")
    @ApiOperation(value = "코드그룹으로 공통 코드 검색")
    public List<CommonCode> searchCode(@RequestParam String codeGroup){
        return sampleService.findAllCommonCodesByGroupCode(codeGroup);
    }
}
