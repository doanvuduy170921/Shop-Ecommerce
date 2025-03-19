package com.example.ShopEcommerce.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.ShopEcommerce.dto.req.AddToCardReq;
import com.example.ShopEcommerce.entity.Cart;
import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.repository.CartRepository;
import com.example.ShopEcommerce.repository.ProductRepository;
import com.example.ShopEcommerce.repository.UserRepository;
import com.example.ShopEcommerce.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public void addToCart(AddToCardReq addToCardReq) {
        User user = userRepository.findById(addToCardReq.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (cartRepository.existsCartByProductId(addToCardReq.getProductId())) {
            Cart cart = cartRepository.findByUserIdAndProductId(addToCardReq.getUserId(), addToCardReq.getProductId());
            cart.setQuantity(cart.getQuantity() + addToCardReq.getQuantity());
            cartRepository.save(cart);
            return;
        }
        Product product = productRepository.findById(addToCardReq.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(addToCardReq.getQuantity());

        cartRepository.save(cart);
    }

    private final Map<Long, Cart> cartItems = new HashMap<>();

    private User getCurrentUser() {
        return userRepository.findById(1L) // Giả định userId = 1, thay bằng logic thực tế
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteCartById(Long cart_id) {
        cartRepository.deleteById(cart_id);
    }

    @Override
    public void updateCart(Long cartId, Integer quantity) {
        {
            Cart cart = cartRepository.findById(cartId).orElse(null);
            if (cart != null) {
                cart.setQuantity(quantity);
                cartRepository.save(cart);
            }
        }
    }

    public Collection<Cart> getCartItems() {
        return cartItems.values();
    }

    @Override
    public List<Cart> findAllCarts() {
        return cartRepository.findAll();
    }

    // @Override
    // public void updateCart(Long cart_id, Integer quantity) {
    // Cart cart = cartRepository.findById(cart_id).orElseThrow(null);
    // cart.setQuantity(cart.getQuantity() + quantity);
    // cartRepository.save(cart);
    // }

    // public void addProductToCart(Long productId, int quantity) {
    // cartItems.computeIfPresent(productId, (id, item) -> {
    // item.increaseQuantity(quantity);
    // return item;
    // });
    // User user = null;
    // Product product = null;
    // cartItems.putIfAbsent(productId, new Cart(product, quantity, user));
    // }

}
