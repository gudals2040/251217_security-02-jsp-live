package kr.java.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    // #(1)-3
    // @GetMapping("/403")
    @PostMapping("/403")
    // @RequestMapping("/403")
    public String forbidden() {
        return "error/403";
    }
}
