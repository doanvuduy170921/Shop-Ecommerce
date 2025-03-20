package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.dto.req.ProductResp;
import com.example.ShopEcommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResp>> searchProducts(@RequestParam String query) {
        List<ProductResp> results = productService.searchProducts(query).stream()
                .map(product -> new ProductResp(product))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
}
