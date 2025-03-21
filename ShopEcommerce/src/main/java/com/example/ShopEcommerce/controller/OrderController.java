package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.Cart;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.repository.CartRepository;
import com.example.ShopEcommerce.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/order")
public class OrderController {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderController(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String order(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "order";
    }

    @PostMapping("/confirm")
    public String orderConfirm(@RequestParam(name = "selectedItems", required = false) String selectedItems,
                               HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (selectedItems == null || selectedItems.isEmpty()) {
            return "redirect:/cart";
        }

        List<Long> cartIds = Arrays.stream(selectedItems.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Cart> selectedCarts = cartRepository.findAllById(cartIds);

        model.addAttribute("selectedCarts", selectedCarts);
        return "order/order-confirm";
    }

    @GetMapping("/success")
    public String orderSuccess() {
        return "order/order-success";
    }
}
