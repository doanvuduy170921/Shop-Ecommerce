package com.example.ShopEcommerce.service;

import java.util.List;

import com.example.ShopEcommerce.dto.resp.CategoryResp;

public interface CategoryService {
    List<CategoryResp> getAllCategories();
}
