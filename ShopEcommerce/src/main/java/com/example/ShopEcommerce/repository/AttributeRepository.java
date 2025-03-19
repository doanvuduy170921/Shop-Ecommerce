package com.example.ShopEcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ShopEcommerce.entity.Attribute;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Integer> {
    // List<Attribute> findAllByCategoryId(Integer categoryId);
    List<Attribute> findByGroupId(Integer groupId);
}
