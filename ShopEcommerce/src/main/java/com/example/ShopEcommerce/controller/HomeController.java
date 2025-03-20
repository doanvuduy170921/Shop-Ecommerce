package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.dto.resp.ProductResp;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;
    @GetMapping("")
    public String home(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        User user = (User) session.getAttribute("user");

        if (user != null && user.getRole() != null && user.getRole().getId() == 2) {
            // Xóa hoàn toàn session
            session.removeAttribute("user");
            redirectAttributes.addFlashAttribute("infoMsg", "Bạn đã đăng xuất khỏi tài khoản quản trị.");
        }

        Page<ProductResp> products = productService.findAll();
        model.addAttribute("products", products);

        return "index";
    }
}
