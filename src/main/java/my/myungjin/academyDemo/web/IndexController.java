package my.myungjin.academyDemo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/sample")
    public String sampleIndex(){
        return "admin";
    }

}


