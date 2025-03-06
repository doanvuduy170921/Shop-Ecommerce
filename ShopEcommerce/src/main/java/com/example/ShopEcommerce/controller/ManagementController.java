package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.service.ProductService;
import com.example.ShopEcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ManagementController {
    private final UserService userService;
    private final ProductService productService;

    public ManagementController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/productManagement")
    public String productManagement(@RequestParam(name = "keyword", required = false) String keyword, Model model){
        List<Product> products = productService.searchProducts(keyword);

        model.addAttribute("products",products);
        model.addAttribute("keyword", keyword);
        return "management/productManagement";
    }

    @GetMapping("/accountManagement")
    public String accountManagement(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<User> users = userService.searchUsers(keyword);

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        return "management/accountManagement";
    }

    @GetMapping("/accountDetail/{id}")
    public String accountDetail(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/api/accountManagement";
        }
        model.addAttribute("user", user);
        return "management/accountDetail";
    }

    @GetMapping("/productDetail/{id}")
    public String productDetail(@PathVariable int id, Model model){
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/api/productManagement";
        }
        model.addAttribute("product", product);
        return "management/productDetail";
    }

    @GetMapping("/addProduct")
    public String addProduct(){
        return "management/addProduct";
    }

    @GetMapping("/infoAccount")
    public String infoAccount(){
        return "management/infoAccount";
    }

    @GetMapping("/updateAccount")
    public String updateAccount(){
        return "management/updateAccount";
    }

}

