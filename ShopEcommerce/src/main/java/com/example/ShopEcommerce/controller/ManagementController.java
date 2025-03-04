package com.example.ShopEcommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagementController {
    @GetMapping("/productManagement")
    public String productManagement(){
        return "management/productManagement.html";
    }

    @GetMapping("/accountManagement")
    public String accountManagement(){
        return "management/accountManagement.html";
    }

    @GetMapping("/accountDetail")
    public String accountDetail(){
        return "management/accountDetail.html";
    }

    @GetMapping("/addProduct")
    public String addProduct(){
        return "management/addProduct.html";
    }

    @GetMapping("/infoAccount")
    public String infoAccount(){
        return "management/infoAccount.html";
    }

}

