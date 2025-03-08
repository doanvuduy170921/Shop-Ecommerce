package com.example.ShopEcommerce.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.example.ShopEcommerce.entity.Product;

import org.springframework.data.domain.Page;

import com.example.ShopEcommerce.dto.resp.ProductResp;
import org.springframework.stereotype.Service;

public interface ProductService {
    Page<ProductResp> getAllProductsByCategoryId(int categoryId, int page, int size);
    ProductResp getProductById(int id);
    Map<String, Object> getAttributesByProductId(int productId);
    List<Product> searchProducts(String keyword);
    Product findById(int id);
    List<String> getImagesByProductId(int productId);
}
