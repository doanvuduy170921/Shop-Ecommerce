package com.example.ShopEcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShopEcommerce.entity.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, Integer> {
    // List<Attribute> findAllByCategoryId(Integer categoryId);
}
