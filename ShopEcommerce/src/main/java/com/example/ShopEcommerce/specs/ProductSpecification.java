package com.example.ShopEcommerce.specs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import com.example.ShopEcommerce.entity.Product;

public class ProductSpecification {

    public static Specification<Product> hasPriceBetween(Integer minPrice, Integer maxPrice) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));

        };
    }
}
