package com.example.ShopEcommerce.repository;

import com.example.ShopEcommerce.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    void deleteByProductId(int productId);
}
