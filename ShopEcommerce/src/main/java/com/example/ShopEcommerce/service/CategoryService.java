package com.example.ShopEcommerce.service;

import java.util.List;

import com.example.ShopEcommerce.dto.resp.CategoryResp;
import com.example.ShopEcommerce.entity.Category;

public interface CategoryService {
    List<CategoryResp> getAllCategories();
    CategoryResp findById(int id);
    Category findCategoryById(int id);
}
