package my.myungjin.academyDemo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

}


