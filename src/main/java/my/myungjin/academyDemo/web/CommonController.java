package my.myungjin.academyDemo.web;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.commonCode.CodeGroup;
import my.myungjin.academyDemo.domain.commonCode.CommonCode;
import my.myungjin.academyDemo.service.commonCode.CommonCodeService;
import my.myungjin.academyDemo.web.request.CodeGroupRequest;
import my.myungjin.academyDemo.web.request.CommonCodeRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommonController {

    private final CommonCodeService sampleService;

    @GetMapping("/codeGroup/list")
    public List<CodeGroup> codeGroups(){
        return sampleService.findAllGroups();
    }

    @PostMapping("/codeGroup")
    public CodeGroup registGroup(@RequestBody CodeGroupRequest request){
        return sampleService.registGroup(request.newCodeGroup());
    }

    @PutMapping("/codeGroup/{id}")
    public CodeGroup modifyCodeGroup(@PathVariable String id, @RequestBody CodeGroupRequest request){
        return sampleService.modifyGroup(id, request.getCode(), request.getNameEng(), request.getNameKor());
    }

    @DeleteMapping("/codeGroup/{id}")
    public void removeCodeGroup(@PathVariable String id){
        sampleService.removeGroup(id);
    }


    @GetMapping("/codeGroup/{id}/commonCode/list")
    public CodeGroup commonCodesByGroupId(@PathVariable String id){
        return sampleService.findAllCommonCodesByGroupId(Id.of(CodeGroup.class, id));
    }

    @PostMapping("/codeGroup/{id}/commonCode")
    public CommonCode newCommonCode(@PathVariable String id, @RequestBody CommonCodeRequest request){
        return sampleService.registCommonCode(Id.of(CodeGroup.class, id), request.newCommonCode(Id.of(CodeGroup.class, id)));
    }

    @PutMapping("/codeGroup/{groupId}/commonCode/{id}")
    public CommonCode modifyCode(@PathVariable String groupId, @PathVariable String id, @RequestBody CommonCodeRequest request){
        return sampleService.modifyCode(Id.of(CodeGroup.class, groupId), Id.of(CommonCode.class, id),
                request.getCode(), request.getNameEng(), request.getNameKor());
    }

    @DeleteMapping("/codeGroup/{groupId}/commonCode/{id}")
    public void removeCode(@PathVariable String groupId, @PathVariable String id){
        sampleService.removeCode(Id.of(CodeGroup.class, groupId), Id.of(CommonCode.class, id));
    }

}
