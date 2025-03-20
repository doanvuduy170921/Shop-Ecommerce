package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.entity.ProductAttribute;
import com.example.ShopEcommerce.repository.ProductAttributeRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductAttributeService {
    private final ProductAttributeRepository productAttributeRepository;

    public ProductAttributeService(ProductAttributeRepository productAttributeRepository) {
        this.productAttributeRepository = productAttributeRepository;
    }

    public ProductAttribute saveProductAttribute(ProductAttribute productAttribute) {
        return productAttributeRepository.save(productAttribute);
    }
    public void deleteProductAttributesByProductId(Long productId) {
        productAttributeRepository.deleteByProductId(productId);
    }
}
