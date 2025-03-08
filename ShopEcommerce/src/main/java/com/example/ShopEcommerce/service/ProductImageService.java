package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.entity.ProductImage;
import com.example.ShopEcommerce.repository.ProductImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public void saveproductImage(ProductImage productImage) {
        productImageRepository.save(productImage);
    }

    public void deleteByProductId(int productId) {
        productImageRepository.deleteByProductId(productId);
    }
}
