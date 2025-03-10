package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.entity.ProductImage;
import com.example.ShopEcommerce.repository.ProductImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public ProductImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    public void saveproductImage(ProductImage productImage) {
        productImageRepository.save(productImage);
    }

    public void deleteByProductId(Long productId) {
        productImageRepository.deleteByProductId(productId);
    }
    public ProductImage findById(Long id) {
        return productImageRepository.findById(Math.toIntExact(id)).orElse(null);
    }
    public List<ProductImage> getImagesByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }
    public void deleteById(Long id) {
        productImageRepository.deleteById(Math.toIntExact(id));
    }
}
