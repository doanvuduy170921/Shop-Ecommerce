package com.example.ShopEcommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ShopEcommerce.entity.Cart;
import com.example.ShopEcommerce.repository.CartRepository;

@Service
public class CartService {
	
	@Autowired
	private CartRepository cartRepository;
	
	public List<Cart> getCarts(){
		return cartRepository.findAll();
	}
}
