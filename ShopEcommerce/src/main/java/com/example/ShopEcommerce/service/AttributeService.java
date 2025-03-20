package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.dto.AttributeDTO;
import com.example.ShopEcommerce.entity.Attribute;
import com.example.ShopEcommerce.entity.ProductAttribute;
import com.example.ShopEcommerce.repository.AttributeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeService {
    private final AttributeRepository attributeRepository;

    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    public List<AttributeDTO> findAttributesByGroupId(int groupId) {
        List<Attribute> attributes = attributeRepository.findByGroupId(groupId);
        return attributes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AttributeDTO convertToDTO(Attribute attribute) {
        AttributeDTO dto = new AttributeDTO();
        dto.setId(attribute.getId());
        dto.setName(attribute.getName());
        return dto;
    }
    public Attribute findAttributeById(int id) {
        return attributeRepository.findById(id).orElse(null);
    }

    public List<Attribute> getAllAttributes() {
        return attributeRepository.findAll();
    }

    @Transactional
    public void deleteAttributeById(Integer id) {
        attributeRepository.deleteById(id);
    }
    public Attribute saveAttribute(Attribute productAttribute) {
        return attributeRepository.save(productAttribute);
    }
    public void deleteUnusedAttributes() {
        attributeRepository.deleteUnusedAttributes();
    }
}
