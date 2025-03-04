package com.example.ShopEcommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {
    @GetMapping("/carts")
    public String cartPage() {
        return "cart/cart"; // Trả về file cart.html
    }
}
