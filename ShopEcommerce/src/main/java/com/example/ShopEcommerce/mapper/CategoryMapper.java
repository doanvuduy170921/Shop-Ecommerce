package com.example.ShopEcommerce.mapper;

import com.example.ShopEcommerce.dto.resp.CategoryResp;
import com.example.ShopEcommerce.entity.Category;

public class CategoryMapper {
    public static CategoryResp toCategoryResp(Category category) {
        CategoryResp categoryResp = new CategoryResp();
        categoryResp.setId(category.getId());
        categoryResp.setName(category.getName());
        return categoryResp;
    }
}
