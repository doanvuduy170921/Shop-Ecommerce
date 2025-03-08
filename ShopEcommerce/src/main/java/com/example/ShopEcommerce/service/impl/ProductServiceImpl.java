package com.example.ShopEcommerce.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.ShopEcommerce.entity.Product;
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
    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(keyword);
        }
        return productRepository.findAll();
    }
    @Override
    public Product findById(int id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    @Override
    public Page<Product> searchProductPaginated(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
