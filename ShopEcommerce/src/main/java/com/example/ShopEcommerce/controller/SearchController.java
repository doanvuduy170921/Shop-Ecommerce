package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.dto.req.ProductResp;
import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.form.RegisterForm;
import com.example.ShopEcommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

//    private final ProductService productService;
    @Autowired
    private ProductService productService; // Service để lấy dữ liệu sản phẩm

//    @GetMapping("/search-page")
//    public String showRegister(Model model) {
//        model.addAttribute("registerForm", new RegisterForm());
//        return "component/search";
//    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResp>> searchProductsApi(@RequestParam String query) {
        List<ProductResp> results = productService.searchProducts(query).stream()
                .map(product -> new ProductResp(product))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search-page")
    public String searchProductsPage(@RequestParam("q") String query,
                                     @RequestParam(value = "productId", required = false) Long productId,
                                     Model model) {
        List<Product> searchResults = productService.searchProducts(query);
        model.addAttribute("products", searchResults);
        model.addAttribute("keyword", query);
        model.addAttribute("resultCount", searchResults.size());

        if (productId != null) {
            model.addAttribute("selectedProduct", productService.getProductById(productId));
        }

        return "component/search"; // Trả về search.html
    }
}