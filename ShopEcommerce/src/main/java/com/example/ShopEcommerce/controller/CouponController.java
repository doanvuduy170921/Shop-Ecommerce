package com.example.ShopEcommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coupons")
public class CouponController {
    @GetMapping("")
    public String getCoupons() {
        return "coupon/AddCoupon";
    }
}
