package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.dto.req.ProductResp;
import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final ProductService productService;

    @GetMapping("/search")
    public ResponseEntity<List<ProductResp>> searchProductsApi(@RequestParam String query) {
        List<ProductResp> results = productService.searchProducts(query).stream()
                .map(product -> new ProductResp(product))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search-page")
    public String searchProductsPage(@RequestParam String q, Model model) {
        List<Product> searchResults = productService.searchProducts(q);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", q);
        model.addAttribute("resultCount", searchResults.size());
        return "search";
    }
}