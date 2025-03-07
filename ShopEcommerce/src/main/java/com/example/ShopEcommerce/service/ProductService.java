package com.example.ShopEcommerce.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.example.ShopEcommerce.dto.resp.ProductResp;

public interface ProductService {
    Page<ProductResp> getAllProductsByCategoryId(int categoryId, int page, int size);
    ProductResp getProductById(int id);
    Map<String, Object> getAttributesByProductId(int productId);
}
