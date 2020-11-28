package my.myungjin.academyDemo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

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


