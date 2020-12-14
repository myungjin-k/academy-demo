package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api/admin/codeGroup")
@RequiredArgsConstructor
@RestController
public class CommonController {

    private final CommonCodeService sampleService;

    @GetMapping("/list")
    @ApiOperation(value = "코드 그룹 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<CodeGroup>> codeGroups(PageRequest pageRequest){
        return OK(sampleService.findAllGroups(pageRequest.of()));
    }

    @PostMapping
    @ApiOperation(value = "코드 그룹 등록")
    public Response<CodeGroup> registGroup(@RequestBody CodeGroupRequest request){
        return OK(sampleService.registGroup(request.newCodeGroup()));

    }

    @PutMapping("/{id}")
    public Response<CodeGroup> modifyCodeGroup(@PathVariable String id, @RequestBody CodeGroupRequest request){
        return OK(
                sampleService.modifyGroup(Id.of(CodeGroup.class, id),
                        request.getCode(),
                        request.getNameEng(),
                        request.getNameKor())
        );
    }

    @DeleteMapping("/{id}")
    public void removeCodeGroup(@PathVariable String id){
        sampleService.removeGroup(Id.of(CodeGroup.class, id));
    }

/*    @GetMapping("/{code}/commonCode/list")
    public Response<CodeGroup> commonCodesByGroupCode(@PathVariable String code){
        return OK(sampleService.findAllCommonCodesByGroupCode(code));
    }*/

/*    @GetMapping("/{id}/commonCode/list")
    public Response<List<CommonCode>> commonCodesByGroupId(@PathVariable String id){
        return OK(sampleService.findAllCommonCodesByGroupId(Id.of(CodeGroup.class, id)));
    }*/


    @GetMapping("/{id}/commonCode/list")
    public Response<Page<CommonCode>> commonCodesByGroupIdWithPage(@PathVariable String id, PageRequest pageRequest){
        return OK(sampleService.findAllCommonCodeByGroupIdWithPage(Id.of(CodeGroup.class, id), pageRequest.of()));
    }

    @PostMapping("/{id}/commonCode")
    public Response<CommonCode> newCommonCode(@PathVariable String id, @RequestBody CommonCodeRequest request){
        return OK(
                sampleService.registCommonCode(Id.of(CodeGroup.class, id),
                        request.newCommonCode(Id.of(CodeGroup.class, id)))
        );
    }

    @PutMapping("/{groupId}/commonCode/{id}")
    public Response<CommonCode> modifyCode(@PathVariable String groupId, @PathVariable String id, @RequestBody CommonCodeRequest request){
        return OK(
                sampleService.modifyCode(Id.of(CodeGroup.class, groupId),
                        Id.of(CommonCode.class, id),
                        request.getCode(),
                        request.getNameEng(),
                        request.getNameKor())
        );
    }

    @DeleteMapping("/{groupId}/commonCode/{id}")
    public void removeCode(@PathVariable String groupId, @PathVariable String id){
        sampleService.removeCode(Id.of(CodeGroup.class, groupId), Id.of(CommonCode.class, id));
    }

}
