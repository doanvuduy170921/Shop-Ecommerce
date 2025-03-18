package com.example.ShopEcommerce.specs;

import org.springframework.data.jpa.domain.Specification;

import com.example.ShopEcommerce.entity.Rating;
import com.example.ShopEcommerce.entity.User;

import jakarta.persistence.criteria.*;

public class RatingSpecification {
    public static Specification<Rating> filterByProduct(Long productId, String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            // Join với bảng User để lấy thêm thông tin người dùng
            Join<Rating, User> userJoin = root.join("user", JoinType.INNER);

            Predicate predicate = criteriaBuilder.conjunction();

            // Lọc theo productId từ Rating (thay vì từ User)
            if (productId != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("product").get("id"), productId));
            }

            // Sắp xếp theo createdAt
            if ("asc".equalsIgnoreCase(sortDirection)) {
                query.orderBy(criteriaBuilder.asc(root.get("createdAt")));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            }

            return predicate;
        };
    }
}


