package com.example.ShopEcommerce.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.example.ShopEcommerce.entity.Product;

import org.springframework.data.domain.Page;

import com.example.ShopEcommerce.dto.resp.ProductResp;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

public interface ProductService {
    Page<ProductResp> getAllProductsByCategoryId(int categoryId, int page, int size, String sortDirection, Integer minPrice, Integer maxPrice);
    ProductResp getProductById(Long id);
    Map<String, Object> getAttributesByProductId(Long productId);
    List<Product> searchProducts(String keyword);
    Product findById(Long id);
    Page<Product> searchProductPaginated(String keyword, Pageable pageable);
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    List<String> getImagesByProductId(Long productId);
    Page<Product> filterProducts(String keyword, String priceRange, Integer categoryId, Pageable pageable);
}
