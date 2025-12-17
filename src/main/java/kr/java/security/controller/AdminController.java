package kr.java.security.controller;

import kr.java.security.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// #(2)-2
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 목록 보기
    @GetMapping
    // import org.springframework.ui.Model;
    public String dashboard(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
        return "admin/dashboard";
    }

    // 토글 -> on => off, off => on
    @PostMapping("/users/{id}/toggle")
    public String toggleUserStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        if (enabled) {
            adminService.disableUser(id);
        } else {
            adminService.enableUser(id);
        }
        return "redirect:/admin";
    }
}
