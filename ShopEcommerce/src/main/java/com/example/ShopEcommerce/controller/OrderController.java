package com.example.ShopEcommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class OrderController {
    @GetMapping("/order")
    public String order() {
        return "order";
    }


    @GetMapping("/order-confirm")
    public String orderConfirm() {
        return "order-confirm";
    }

    @GetMapping("/order-success")
    public String orderSuccess() {
        return "order-success";
    }
}
