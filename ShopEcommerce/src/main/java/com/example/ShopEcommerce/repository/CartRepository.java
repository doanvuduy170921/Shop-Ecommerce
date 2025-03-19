package com.example.ShopEcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ShopEcommerce.entity.Cart;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserIdAndProductId(Long userId, Long productId);
    void deleteAllByUserId(Long userId);
    void deleteAllByProductId(Long productId);
    boolean existsCartByProductId(Long productId);
    Cart findCartByProductId(Long productId);
    List<Cart> findByUserId(Long userId);
}
