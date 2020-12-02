package my.myungjin.academyDemo.web.controller;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.service.admin.CommonCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final CommonCodeService commonCodeService;

    @GetMapping("/")
    public String main(Model model){
        CodeGroup items =
                commonCodeService.findAllCommonCodesByGroupId(Id.of(CodeGroup.class, "246fa96f9b634a56aaac5884de186ebc"));
        model.addAttribute("items", items.getCommonCodes());
        return "index";
    }

    @GetMapping("/adminLogin")
    public String sampleIndex(){
        return "admin";
    }

    @GetMapping("/login")
    public String loginIndex(){
        return "member";
    }

    @GetMapping(path = "/changePassword/{id}")
    public String changePasswordIndex(@PathVariable String id, Model model)
    {
        model.addAttribute("id", id);
        return "changePassword";
    }

}


