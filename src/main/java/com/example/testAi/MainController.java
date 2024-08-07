package com.example.testAi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "todo";
    }

    @GetMapping("/todo")
    public String test() {
        return "todo";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}