package com.example.ShopEcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShopEcommerce.entity.Attribute;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute, Integer> {
    // List<Attribute> findAllByCategoryId(Integer categoryId);
    List<Attribute> findByGroupId(Integer groupId);
    @Modifying
    @Query("DELETE FROM Attribute a WHERE a.id NOT IN (SELECT DISTINCT pa.attribute.id FROM ProductAttribute pa)")
    void deleteUnusedAttributes();
}
