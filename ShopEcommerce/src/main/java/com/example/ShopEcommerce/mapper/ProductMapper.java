package com.example.ShopEcommerce.mapper;

import com.example.ShopEcommerce.dto.resp.ProductResp;
import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.entity.Rating;
import com.example.ShopEcommerce.repository.RatingRepository;

public class ProductMapper {
    public static ProductResp toProductResp(Product product, RatingRepository ratingRepository) {
        ProductResp productResp = new ProductResp();
        productResp.setId(product.getId());
        productResp.setName(product.getName());
        productResp.setPrice(product.getPrice());
        productResp.setDescription(product.getDescription());
        productResp.setThumbnail(product.getThumbnail());
        if (ratingRepository != null) {
            Double averageRating = ratingRepository.findAverageRatingByProductId(product.getId());
            productResp.setAverageRating(averageRating);
            Integer totalRating = ratingRepository.countByProductId(product.getId());
            productResp.setTotalRating(totalRating);
        }
        productResp.setCategory(CategoryMapper.toCategoryResp(product.getCategory()));
        return productResp;
    }
}
