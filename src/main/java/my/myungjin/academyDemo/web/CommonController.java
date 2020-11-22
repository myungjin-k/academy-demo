package my.myungjin.academyDemo.web;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.service.sample.SampleService;
import my.myungjin.academyDemo.web.request.CodeGroupRequest;
import my.myungjin.academyDemo.web.request.CommonCodeRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommonController {

    private final SampleService sampleService;

    @GetMapping("/codeGroup/list")
    public List<CodeGroup> codeGroups(){
        return sampleService.findAllGroups();
    }

    @PostMapping("/codeGroup")
    public CodeGroup newCodeGroup(@RequestBody CodeGroupRequest request){
        return sampleService.saveGroup(request.newCodeGroup());
    }

    @PutMapping("/codeGroup/{id}")
    public CodeGroup modifyCodeGroup(@PathVariable String id, @RequestBody CodeGroupRequest request){
        return sampleService.modifyGroup(id, request.getCode(), request.getNameEng(), request.getNameKor());
    }

    @DeleteMapping("/codeGroup/{id}")
    public void removeCodeGroup(@PathVariable String id){
        sampleService.deleteGroup(id);
    }


    @GetMapping("/codeGroup/{id}/commonCodes/list")
    public List<CommonCode> commonCodesByGroupId(@PathVariable String id){
        return sampleService.findAllCommonCodesByGroupId(Id.of(CodeGroup.class, id));
    }

    @PostMapping("/codeGroup/{id}/commonCodes")
    public CommonCode newCommonCode(@PathVariable String id, @RequestBody CommonCodeRequest request){
        return sampleService.saveCommonCode(request.newCommonCode(id));
    }
}
