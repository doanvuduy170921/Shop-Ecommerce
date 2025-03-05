package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "Auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            if (authService.existsByEmail(user.getEmail())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Email đã tồn tại!");
                return "redirect:/register";
            }

            authService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Lỗi khi đăng ký người dùng: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Đăng ký thất bại! Vui lòng thử lại.");
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "Auth/login";
    }
}
