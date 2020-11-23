package my.myungjin.academyDemo.web.controller;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.service.commonCode.CommonCodeService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.CodeGroupRequest;
import my.myungjin.academyDemo.web.request.CommonCodeRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RestController
public class CommonController {

    private final CommonCodeService sampleService;

    @GetMapping("/codeGroup/list")
    public Response<List<CodeGroup>> codeGroups(){
        return OK(sampleService.findAllGroups());
    }

    @PostMapping("/codeGroup")
    public Response<CodeGroup> registGroup(@RequestBody CodeGroupRequest request){
        return OK(sampleService.registGroup(request.newCodeGroup()));

    }

    @PutMapping("/codeGroup/{id}")
    public Response<CodeGroup> modifyCodeGroup(@PathVariable String id, @RequestBody CodeGroupRequest request){
        return OK(
                sampleService.modifyGroup(id,
                        request.getCode(),
                        request.getNameEng(),
                        request.getNameKor())
        );
    }

    @DeleteMapping("/codeGroup/{id}")
    public void removeCodeGroup(@PathVariable String id){
        sampleService.removeGroup(id);
    }


    @GetMapping("/codeGroup/{id}/commonCode/list")
    public Response<CodeGroup> commonCodesByGroupId(@PathVariable String id){
        return OK(sampleService.findAllCommonCodesByGroupId(Id.of(CodeGroup.class, id)));
    }

    @PostMapping("/codeGroup/{id}/commonCode")
    public Response<CommonCode> newCommonCode(@PathVariable String id, @RequestBody CommonCodeRequest request){
        return OK(
                sampleService.registCommonCode(Id.of(CodeGroup.class, id),
                        request.newCommonCode(Id.of(CodeGroup.class, id)))
        );
    }

    @PutMapping("/codeGroup/{groupId}/commonCode/{id}")
    public Response<CommonCode> modifyCode(@PathVariable String groupId, @PathVariable String id, @RequestBody CommonCodeRequest request){
        return OK(
                sampleService.modifyCode(Id.of(CodeGroup.class, groupId),
                        Id.of(CommonCode.class, id),
                        request.getCode(),
                        request.getNameEng(),
                        request.getNameKor())
        );
    }

    @DeleteMapping("/codeGroup/{groupId}/commonCode/{id}")
    public void removeCode(@PathVariable String groupId, @PathVariable String id){
        sampleService.removeCode(Id.of(CodeGroup.class, groupId), Id.of(CommonCode.class, id));
    }

}
