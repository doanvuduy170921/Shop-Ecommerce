package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.dto.resp.ProductResp;
import com.example.ShopEcommerce.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;
    @GetMapping("")
    public String home(Model model) {
        Page<ProductResp> products = productService.findAll();
        model.addAttribute("products", products);
        return "index";
    }
}
