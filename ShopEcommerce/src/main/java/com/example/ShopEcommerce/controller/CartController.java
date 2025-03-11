package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.Cart;
import com.example.ShopEcommerce.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Past;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @GetMapping("/carts")
    public String cartPage(Model model) {
        List<Cart> cartList = cartService.findAllCarts();
        model.addAttribute("cartList", cartList);
        return "cart/cart"; // Trả về file cart.html
    }

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        cartService.addProductToCart(productId, quantity);
        return "redirect:/cart/view"; // Chuyển hướng đến trang giỏ hàng
    }

    @GetMapping("/view")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        return "cart"; // Hiển thị trang giỏ hàng (cart.html)
    }

//    /product/details/add-to-cart
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("cart_id") Long cart_id, HttpSession session) {
        System.out.println("remove cart");
        cartService.deleteCartById(cart_id);
    return "redirect:/cart/carts";
    }

//    @PostMapping("/update")
//    public String updateCart(@RequestParam("cart_id") Long cart_id, @RequestParam("quantity") Integer quantity,  HttpSession session) {
//        System.out.println("update cart");
//        cartService.updateCart(cart_id, quantity);
//        return "redirect:/cart/carts";
//    }
}
