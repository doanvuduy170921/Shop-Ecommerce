package com.example.ShopEcommerce.repository;

import com.example.ShopEcommerce.entity.ProductImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends CrudRepository<ProductImage, Integer> {
    @Query(value = "SELECT image_url  FROM product_images WHERE product_id = :id;", nativeQuery = true)
    List<String> findAllByProductId(@Param("id") Long id);
}
