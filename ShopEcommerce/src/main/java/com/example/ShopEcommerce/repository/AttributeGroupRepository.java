package com.example.ShopEcommerce.repository;

import com.example.ShopEcommerce.entity.AttributeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeGroupRepository extends JpaRepository<AttributeGroup, Integer> {
}
