package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.service.ProductService;
import com.example.ShopEcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
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
            return "redirect:/admin/accountManagement";
        }
        model.addAttribute("user", user);
        return "management/accountDetail";
    }

    @GetMapping("/productDetail/{id}")
    public String productDetail(@PathVariable int id, Model model){
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/productManagement";
        }
        model.addAttribute("product", product);
        return "management/productDetail";
    }

    @GetMapping("/addProduct")
    public String addProduct(){
        return "management/addProduct";
    }
}

