package com.example.ShopEcommerce.service;

import org.springframework.data.domain.Page;

import com.example.ShopEcommerce.entity.Rating;

public interface RatingService {
    public void saveRating(Long userId, Long productId, int rating, String review);
    Page<Rating> getRatings(Long productId, String sortDirection, int page, int size);
    Double getProductIdAverageRating(Long productId);
}
