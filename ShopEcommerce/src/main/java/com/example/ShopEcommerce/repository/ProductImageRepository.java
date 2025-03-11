package com.example.ShopEcommerce.repository;

import java.util.List;


import com.example.ShopEcommerce.entity.ProductImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ProductImageRepository extends CrudRepository<ProductImage, Integer> {
    @Query(value = "SELECT image_url  FROM product_images WHERE product_id = :id;", nativeQuery = true)
    List<String> findAllByProductId(@Param("id") Long id);
    List<ProductImage> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
