package com.example.ShopEcommerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.ShopEcommerce.dto.resp.CategoryResp;
import com.example.ShopEcommerce.service.CategoryService;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private CategoryService categoryService;

    @ModelAttribute("global_categories")
    public List<CategoryResp> globalCategories() {
        List<CategoryResp> categories = categoryService.getAllCategories();
        return categories;
    }
}
