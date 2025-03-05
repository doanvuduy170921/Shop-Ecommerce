package com.example.ShopEcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShopEcommerce.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
	
}
