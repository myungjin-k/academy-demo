package my.myungjin.academyDemo.web.controller;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.security.User;
import my.myungjin.academyDemo.service.admin.CommonCodeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final CommonCodeService commonCodeService;

    private List<CommonCode> itemCategories ;

    @GetMapping("/index")
    public String main(Model model, @AuthenticationPrincipal Authentication authentication){
        itemCategories = commonCodeService.findAllCommonCodesByGroupId
                (Id.of(CodeGroup.class, "246fa96f9b634a56aaac5884de186ebc"));
        model.addAttribute("items", itemCategories);

        if(authentication != null && authentication.isAuthenticated()){
            User loginUser = (User) authentication.getDetails();
            model.addAttribute("loginUser", loginUser);
            if(loginUser.getRole().name().equalsIgnoreCase("ADMIN"))
                model.addAttribute("isAdmin", true);
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginIndex(Model model)
    {
        itemCategories = commonCodeService.findAllCommonCodesByGroupId
                (Id.of(CodeGroup.class, "246fa96f9b634a56aaac5884de186ebc"));
        model.addAttribute("items", itemCategories);
        return "member";
    }

    @GetMapping(path = "/changePassword/{id}")
    public String changePasswordIndex(@PathVariable String id, Model model)
    {
        model.addAttribute("id", id);
        return "changePassword";
    }

    @GetMapping("/myPage")
    public String myPageIndex(Model model, @AuthenticationPrincipal Authentication authentication){
        itemCategories = commonCodeService.findAllCommonCodesByGroupId
                (Id.of(CodeGroup.class, "246fa96f9b634a56aaac5884de186ebc"));
        model.addAttribute("items", itemCategories);

        if(authentication != null && authentication.isAuthenticated()){
            User loginUser = (User) authentication.getDetails();
            model.addAttribute("loginUser", loginUser);
            if(loginUser.getRole().name().equalsIgnoreCase("ADMIN"))
                model.addAttribute("isAdmin", true);
        }
        return "mypage";
    }

    @GetMapping("/adminLogin")
    public String adminLoginIndex(){

        return "admin/admin";
    }

    @GetMapping("/admin/codeIndex")
    public String adminCodeIndex(Model model, @AuthenticationPrincipal Authentication authentication){

        if(authentication != null && authentication.isAuthenticated()){
            User loginUser = (User) authentication.getDetails();
            model.addAttribute("loginUser", loginUser);
            if(loginUser.getRole().name().equalsIgnoreCase("ADMIN"))
                model.addAttribute("isAdmin", true);
        }
        return "admin/code";
    }

    @GetMapping("/admin/itemIndex")
    public String adminItemIndex(Model model, @AuthenticationPrincipal Authentication authentication){

        if(authentication != null && authentication.isAuthenticated()){
            User loginUser = (User) authentication.getDetails();
            model.addAttribute("loginUser", loginUser);
            if(loginUser.getRole().name().equalsIgnoreCase("ADMIN"))
                model.addAttribute("isAdmin", true);
        }
        return "admin/item";
    }
}


