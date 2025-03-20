package com.example.ShopEcommerce.repository;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ShopEcommerce.entity.ProductAttribute;
@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
    List<ProductAttribute> findAllByProductId(Long productId);
    @Transactional
    void deleteByProductId(Long productId);
}
