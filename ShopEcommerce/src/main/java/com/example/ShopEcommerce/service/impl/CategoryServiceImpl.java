package com.example.ShopEcommerce.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ShopEcommerce.dto.resp.CategoryResp;
import com.example.ShopEcommerce.entity.Category;
import com.example.ShopEcommerce.mapper.CategoryMapper;
import com.example.ShopEcommerce.repository.CategoryRepository;
import com.example.ShopEcommerce.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResp> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::toCategoryResp).collect(Collectors.toList());
    }

}
