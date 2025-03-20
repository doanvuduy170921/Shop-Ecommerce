package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class OrderController {
    @GetMapping("/order")
    public String order(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "order";
    }


    @GetMapping("/order-confirm")
    public String orderConfirm() {
        return "order/order-confirm";
    }

    @GetMapping("/order-success")
    public String orderSuccess() {
        return "order/order-success";
    }
}
