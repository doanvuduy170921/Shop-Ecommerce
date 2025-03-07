package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.dto.req.AddToCardReq;

public interface CartService {
    void addToCart(AddToCardReq addToCardReq);
}
