package my.myungjin.academyDemo.web.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.aws.S3Client;
import my.myungjin.academyDemo.commons.AttachedFile;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.ItemStatus;
import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.security.User;
import my.myungjin.academyDemo.service.admin.CommonCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final CommonCodeService commonCodeService;

    private List<CommonCode> itemCategories;

    private final S3Client s3Client;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/test/upload")
    public String imgUploadTest(@RequestPart MultipartFile imageFile) throws IOException {
        String imageUrl = null;
        AttachedFile attachedImgFile = toAttachedFile(imageFile);
        assert attachedImgFile != null;
        String key = attachedImgFile.randomName("test", "jpeg");
        try {
            imageUrl = s3Client.upload(attachedImgFile.inputStream(),
                    attachedImgFile.length(),
                    key,
                    attachedImgFile.getContentType(),
                    null);
        } catch (AmazonS3Exception e) {
            log.warn("Amazon S3 error (key: {}): {}", key, e.getMessage(), e);
        }
        return imageUrl;
    }

    @DeleteMapping("/test/remove")
    public String imgRemoveTest(@RequestPart MultipartFile imageFile) throws IOException {
        String imageUrl = null;
        AttachedFile attachedImgFile = toAttachedFile(imageFile);
        assert attachedImgFile != null;
        String key = attachedImgFile.randomName("test", "jpeg");
        try {
            imageUrl = s3Client.upload(attachedImgFile.inputStream(),
                    attachedImgFile.length(),
                    key,
                    attachedImgFile.getContentType(),
                    null);
        } catch (AmazonS3Exception e) {
            log.warn("Amazon S3 error (key: {}): {}", key, e.getMessage(), e);
        }
        return imageUrl;
    }


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
    public String adminOrderIndex(Model model, @AuthenticationPrincipal Authentication authentication){
        setItemCategories(model);
        setLoginUser(model, authentication);
        model.addAttribute("isAdmin", true);
        model.addAttribute("deliveryStatus", DeliveryStatus.values());
        return "admin/order";
    }

    @GetMapping("/admin/reviewIndex")
    public String adminReviewIndex(Model model, @AuthenticationPrincipal Authentication authentication){
        setItemCategories(model);
        setLoginUser(model, authentication);
        model.addAttribute("isAdmin", true);
        model.addAttribute("deliveryStatus", DeliveryStatus.values());
        return "admin/review";
    }

    @GetMapping("/admin/memberIndex")
    public String adminMemberIndex(Model model, @AuthenticationPrincipal Authentication authentication){
        setItemCategories(model);
        setLoginUser(model, authentication);
        model.addAttribute("isAdmin", true);
        model.addAttribute("deliveryStatus", DeliveryStatus.values());
        return "admin/member";
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


