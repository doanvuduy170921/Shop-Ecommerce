package com.example.ShopEcommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagementController {
    @GetMapping("/productManagement")
    public String productManagement(){
        return "productManagement";
    }

    @GetMapping("/accountManagement")
    public String accountManagement(){
        return "accountManagement";
    }
}

