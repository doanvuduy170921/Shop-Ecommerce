package com.example.ShopEcommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ShopEcommerce.entity.Cart;
import com.example.ShopEcommerce.service.CartService;

///api/carts
@Controller
@RequestMapping("/api")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
    @GetMapping("/carts")
    public String cartPage() {
        return "cart/cart"; // Trả về file cart.html
    }
    
//    @GetMapping
//    public List<Cart> getCart(){
//    	return cartService.getCarts();
//    }
    
    
}
