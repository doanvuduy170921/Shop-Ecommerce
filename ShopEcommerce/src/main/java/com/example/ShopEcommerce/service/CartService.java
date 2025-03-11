package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.dto.req.AddToCardReq;
import com.example.ShopEcommerce.entity.Cart;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CartService {
    void addToCart(AddToCardReq addToCardReq);

    final Map<Long, Cart> cartItems = new HashMap<>();

    Collection<Cart> getCartItems();

    List<Cart> findAllCarts();

    void addProductToCart(Long productId, int quantity);

    void deleteCartById(Long cart_id);

    void updateCart(Long cart_id, Integer quantity);
}
