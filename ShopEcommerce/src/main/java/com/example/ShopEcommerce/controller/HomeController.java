package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.dto.resp.CategoryResp;
import com.example.ShopEcommerce.service.CategoryService;
import com.example.ShopEcommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final CategoryService categoryService;
    @GetMapping("")
    public String home(Model model) {
        List<CategoryResp> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "index";
    }
}
