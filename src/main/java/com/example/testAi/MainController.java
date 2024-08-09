package com.example.testAi;

import com.example.testAi.user.global.rp.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final Rq request;

    @GetMapping("/")
    public String root() {
        if (request.isLogin()) {
            return "redirect:/subject/main";
        }
        return "start";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/subject/main";
    }

    @GetMapping("/login/sucess")
    public String sucess() { return "redirect:/subject/main"; }

}