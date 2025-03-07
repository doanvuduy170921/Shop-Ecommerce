package com.example.ShopEcommerce.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ShopEcommerce.dto.resp.ProductResp;
import com.example.ShopEcommerce.entity.ProductAttribute;
import com.example.ShopEcommerce.mapper.ProductMapper;
import com.example.ShopEcommerce.repository.ProductAttributeRepository;
import com.example.ShopEcommerce.repository.ProductRepository;
import com.example.ShopEcommerce.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductAttributeRepository productAttributeRepository;

    @Override
    public Page<ProductResp> getAllProductsByCategoryId(int categoryId, int page, int size) {
        // TODO Auto-generated method stub
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllByCategoryId(categoryId, pageable).map(ProductMapper::toProductResp);
    }

    @Override
    public ProductResp getProductById(int id) {
        // TODO Auto-generated method stub
        return ProductMapper.toProductResp(productRepository.findById(id).orElse(null));
    }

    @Override
    public Map<String, Object> getAttributesByProductId(int productId) {
        // TODO Auto-generated method stub
        List<ProductAttribute> productAttributes = productAttributeRepository.findAllByProductId(productId);
        return productAttributes.stream().collect(Collectors.toMap(
            productAttribute -> productAttribute.getAttribute().getName(),
            ProductAttribute::getValue
        ));
    }

}
