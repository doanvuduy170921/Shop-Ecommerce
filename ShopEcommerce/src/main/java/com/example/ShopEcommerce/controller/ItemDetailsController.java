package com.example.ShopEcommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/item-details")
public class ItemDetailsController {

    @GetMapping("")
    public String getMethodName() {
        return "ItemDetails";
    }
    
}
