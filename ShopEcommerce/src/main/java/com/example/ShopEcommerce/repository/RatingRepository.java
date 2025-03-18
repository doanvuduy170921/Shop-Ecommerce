package com.example.ShopEcommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ShopEcommerce.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long>, JpaSpecificationExecutor<Rating> {

    Optional<Rating> findByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Rating r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.product.id = :productId")
    Integer countByProductId(Long productId);
    @Query("SELECT r.rating, COUNT(r) FROM Rating r WHERE r.product.id = :productId GROUP BY r.rating")
    List<Object[]> countRatingsByStar(@Param("productId") Long productId);
}
