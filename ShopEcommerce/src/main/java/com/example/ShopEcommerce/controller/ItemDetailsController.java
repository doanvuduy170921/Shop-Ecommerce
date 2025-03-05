package com.example.ShopEcommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/items")
public class ItemDetailsController {

    @GetMapping("/1")
    public String getMethodName() {
        return "ItemDetails";
    }
    
    @GetMapping("")
    public String getListItems() {
        return "ListItems";
    }
    
}
