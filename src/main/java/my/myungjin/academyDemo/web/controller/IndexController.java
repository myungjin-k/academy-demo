package my.myungjin.academyDemo.web.controller;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.ItemStatus;
import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.security.User;
import my.myungjin.academyDemo.service.admin.CommonCodeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final CommonCodeService commonCodeService;

    private List<CommonCode> itemCategories ;

    @GetMapping("/mall/index")
    public String main(Model model, @AuthenticationPrincipal Authentication authentication){
        setItemCategories(model);
        if(authentication != null) {
            User loginUser = (User) authentication.getDetails();
            if(Role.MEMBER.equals(loginUser.getRole()))
                model.addAttribute("loginUser", loginUser);
        }
        return "index";
    }

    @GetMapping("/mall/login")
    public String loginIndex(@RequestParam(required = false) String redirectUri, Model model)
    {
        if(redirectUri != null)
            model.addAttribute("redirectUri", redirectUri);
        setItemCategories(model);
        return "member";
    }

    @GetMapping(path = "/mall/changePassword/{id}")
    public String changePasswordIndex(@PathVariable String id, Model model)
    {
        model.addAttribute("id", id);
        return "changePassword";
    }

    @GetMapping("/mall/myPage")
    public String myPageIndex(Model model, @AuthenticationPrincipal Authentication authentication){
        setItemCategories(model);
        if(authentication != null){
            User loginUser = (User) authentication.getDetails();
            if(Role.MEMBER.equals(loginUser.getRole()))
                model.addAttribute("loginUser", loginUser);
        }
        return "mypage";
    }

    @GetMapping("/admin/login")
    public String adminLoginIndex(){
        return "admin/admin";
    }

    @GetMapping("/admin/codeIndex")
    public String adminCodeIndex(Model model, @AuthenticationPrincipal Authentication authentication){
        setLoginUser(model, authentication);
        model.addAttribute("isAdmin", true);
        return "admin/code";
    }

    @GetMapping("/admin/itemIndex")
    public String adminItemIndex(Model model, @AuthenticationPrincipal Authentication authentication){
        setItemCategories(model);
        setLoginUser(model, authentication);
        model.addAttribute("isAdmin", true);
        model.addAttribute("itemStatus", ItemStatus.values());
        return "admin/item";
    }

    @GetMapping("/admin/orderIndex")
    public String adminORderIndex(Model model, @AuthenticationPrincipal Authentication authentication){
        setItemCategories(model);
        setLoginUser(model, authentication);
        model.addAttribute("isAdmin", true);
        model.addAttribute("deliveryStatus", DeliveryStatus.values());
        return "admin/order";
    }

    private void setItemCategories(Model model){
        itemCategories = commonCodeService.findAllCommonCodesByGroupId
                (Id.of(CodeGroup.class, "246fa96f9b634a56aaac5884de186ebc"));
        model.addAttribute("items", itemCategories);
    }

    private void setLoginUser(Model model, @AuthenticationPrincipal Authentication authentication){
        if(authentication != null){
            User loginUser = (User) authentication.getDetails();
            model.addAttribute("loginUser", loginUser);
        }
    }
}


