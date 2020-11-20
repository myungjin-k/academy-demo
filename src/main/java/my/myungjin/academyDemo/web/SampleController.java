package my.myungjin.academyDemo.web;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.service.sample.SampleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("/sample/codeGroups/list")
    public List<CodeGroup> allCodeGroups(){
        return sampleService.findAllGroups();
    }

}
