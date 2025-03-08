package com.example.ShopEcommerce.service.impl;

import java.nio.channels.UnsupportedAddressTypeException;

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
        // TODO Auto-generated method stub
        User user = userRepository.findById(addToCardReq.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(addToCardReq.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(addToCardReq.getQuantity());
        
        cartRepository.save(cart);
    }

}
