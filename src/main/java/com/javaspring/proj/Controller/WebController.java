package com.javaspring.proj.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping(value="/")
    public String mainpage(){
        return "index.html";
    }
}
