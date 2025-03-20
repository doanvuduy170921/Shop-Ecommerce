package com.example.ShopEcommerce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.ShopEcommerce.entity.Attribute;
import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.repository.AttributeRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ShopEcommerce.dto.resp.ProductResp;
import com.example.ShopEcommerce.entity.ProductAttribute;
import com.example.ShopEcommerce.mapper.ProductMapper;
import com.example.ShopEcommerce.repository.ProductAttributeRepository;
import com.example.ShopEcommerce.repository.ProductImageRepository;
import com.example.ShopEcommerce.repository.ProductRepository;
import com.example.ShopEcommerce.repository.RatingRepository;
import com.example.ShopEcommerce.service.ProductService;
import com.example.ShopEcommerce.specs.ProductSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AttributeRepository attributeRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final ProductImageRepository productImageRepository;
    private final RatingRepository ratingRepository;

    @Override
    public Page<ProductResp> getAllProductsByCategoryId(int categoryId, int page, int size, String sortDirection,
            Integer minPrice, Integer maxPrice, String keyword) {
        // TODO Auto-generated method stub
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by("price").descending()
                : Sort.by("price").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Product> spec = Specification.where(null);
        
        // Ưu tiên lọc theo categoryId trước
        if (categoryId > 0) {
            spec = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        }
    
        // Sau đó mới lọc theo khoảng giá nếu có
        if (minPrice != null || maxPrice != null) {
            Specification<Product> priceSpec = ProductSpecification.hasPriceBetween(minPrice, maxPrice);
            spec = spec.and(priceSpec);
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            Specification<Product> keywordSpec = (root, query, criteriaBuilder) -> 
                criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%")
                );
            spec = spec.and(keywordSpec);
        }

        return productRepository.findAll(spec, pageable).map(product -> ProductMapper.toProductResp(product, ratingRepository));
    }

    @Override
    public ProductResp getProductById(Long id) {
        // TODO Auto-generated method stub
        return ProductMapper.toProductResp(productRepository.findById(id).orElse(null), ratingRepository);
    }

    @Override
    public Map<String, Object> getAttributesByProductId(Long productId) {
        // TODO Auto-generated method stub
        List<ProductAttribute> productAttributes = productAttributeRepository.findAllByProductId(productId);
        return productAttributes.stream().collect(Collectors.toMap(
                productAttribute -> productAttribute.getAttribute().getName(),
                ProductAttribute::getValue));
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(keyword);
        }
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
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
    public void deleteProduct(Long id) {
        productAttributeRepository.deleteByProductId(id);
//        attributeRepository.deleteUnusedAttributes();
        productRepository.deleteById(id);
    }

    @Override
    public List<String> getImagesByProductId(Long productId) {
        // TODO Auto-generated method stub
        List<String> images = productImageRepository.findByProductId(productId).stream()
                .map(productImage -> productImage.getImageUrl())
                .collect(Collectors.toList());
        images.add(0, productRepository
                .findById(productId)
                .map(product -> product.getThumbnail())
                .orElse(null));
        return images;
    }

    @Override
    public Page<Product> filterProducts(String keyword, String priceRange, Integer categoryId, Pageable pageable) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo từ khóa
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + keyword.toLowerCase() + "%"));
            }

            // Lọc theo khoảng giá
            if (priceRange != null && !priceRange.isEmpty()) {
                switch (priceRange) {
                    case "under5m":
                        predicates.add(criteriaBuilder.lessThan(root.get("price"), 5000000));
                        break;
                    case "5mto20m":
                        predicates.add(criteriaBuilder.between(root.get("price"), 5000000, 20000000));
                        break;
                    case "above20m":
                        predicates.add(criteriaBuilder.greaterThan(root.get("price"), 20000000));
                        break;
                }
            }

            // Lọc theo danh mục
            if (categoryId != null && categoryId > 0) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return productRepository.findAll(spec, pageable);
    }

    @Override
    public Page<ProductResp> findAll() {
        // TODO Auto-generated method stub
        Sort sort = Sort.by("price").ascending();
        Pageable pageable = PageRequest.of(0, 4, sort);
        return productRepository.findAll(pageable).map(product -> ProductMapper.toProductResp(product, ratingRepository));
    }

    @Override
    public List<Attribute> getAllAttributes() {
        return attributeRepository.findAll();
    }

    @Override
    public List<ProductAttribute> getAllProductAttributes() {
        return productAttributeRepository.findAll();
    }
}
