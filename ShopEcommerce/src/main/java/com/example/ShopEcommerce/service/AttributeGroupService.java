package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.entity.AttributeGroup;
import com.example.ShopEcommerce.repository.AttributeGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AttributeGroupService {
    private final AttributeGroupRepository attributeGroupRepository;

    public AttributeGroupService(AttributeGroupRepository attributeGroupRepository) {
        this.attributeGroupRepository = attributeGroupRepository;
    }

    public List<AttributeGroup> getAllAttributeGroups() {
        return attributeGroupRepository.findAll();
    }
}
