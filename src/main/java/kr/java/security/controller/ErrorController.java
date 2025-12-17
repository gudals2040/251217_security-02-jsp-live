package kr.java.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    // #(1)-3
    // @GetMapping("/403")
    @PostMapping("/403")
    // @RequestMapping("/403")
    public String forbidden(
            @RequestAttribute(name = "errorMessage", required = false)
            String errorMessage,
            Model model
    ) {
        if (errorMessage == null) {
            errorMessage = "이 페이지에 접근할 권한이 없습니다";
        }
        return "error/403";
    }
}
