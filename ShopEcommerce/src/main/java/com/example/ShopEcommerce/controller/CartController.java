package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.Cart;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Trang giỏ hàng
    @GetMapping("/carts")
    public String cartPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Cart> cartList = cartService.findCartsbyUserid(user.getId());


        int totalQuantity = cartList.stream().mapToInt(Cart::getQuantity).sum();

        model.addAttribute("cartList", cartList);
        model.addAttribute("totalCartQuantity", totalQuantity);
        session.setAttribute("totalCartQuantity", totalQuantity); // Lưu vào session để Thymeleaf sử dụng
        return "cart/cart";
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("cart_id") Long cart_id, HttpSession session) {
        cartService.deleteCartById(cart_id);
        updateCartSession(session); // Cập nhật số lượng tổng
        return "redirect:/cart/carts";
    }

    // API trả về số lượng sản phẩm trong giỏ hàng cho AJAX
    @GetMapping("/getTotalQuantity")
    @ResponseBody
    public int getTotalCartQuantity(HttpSession session) {
        return (int) session.getAttribute("totalCartQuantity");
    }

    // Cập nhật session khi có thay đổi giỏ hàng
    private void updateCartSession(HttpSession session) {
        List<Cart> cartList = cartService.findCartsbyUserid(((User) session.getAttribute("user")).getId());
        int totalQuantity = cartList.stream().mapToInt(Cart::getQuantity).sum();
        session.setAttribute("totalCartQuantity", totalQuantity);
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateCart(@RequestParam("cart_id") Long cartId, @RequestParam("quantity") Integer quantity, HttpSession session) {
        if (quantity < 1) {
            return "error"; // Không cho phép số lượng < 1
        }

        cartService.updateCart(cartId, quantity);
        updateCartSession(session); // Cập nhật lại session để cập nhật tổng số lượng
        return "success";
    }

}
