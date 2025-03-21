package com.example.ShopEcommerce.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.entity.Rating;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.repository.RatingRepository;
import com.example.ShopEcommerce.service.RatingService;
import com.example.ShopEcommerce.specs.RatingSpecification;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;

    @Override
    public void saveRating(Long userId, Long productId, int ratingValue, String review) {
        Rating rating = ratingRepository.findByUserIdAndProductId(userId, productId)
                    .orElse(new Rating()); // Nếu không có, tạo mới

        rating.setUser(new User(userId));
        rating.setProduct(new Product(productId));
        rating.setRating(ratingValue);
        rating.setReview(review);

        ratingRepository.save(rating);
    }

    @Override
    public Page<Rating> getRatings(Long productId, String sortDirection, int page, int size) {
        // TODO Auto-generated method stub
        Pageable pageable = PageRequest.of(page, size);
        Specification<Rating> spec = RatingSpecification.filterByProduct(productId, sortDirection);
        return ratingRepository.findAll(spec, pageable);   
    }

    @Override
    public Double getProductIdAverageRating(Long productId) {
        // TODO Auto-generated method stub
        return ratingRepository.findAverageRatingByProductId(productId);
    }

    @Override
    public Integer countRatingsByProductId(Long productId) {
        // TODO Auto-generated method stub
        return ratingRepository.countByProductId(productId);
    }

    @Override
    public Map<Integer, Long> countRatings(Long productId) {
        List<Object[]> results = ratingRepository.countRatingsByStar(productId);

        return results.stream()
            .collect(Collectors.toMap(
                obj -> (Integer) obj[0],  // rating (số sao)
                obj -> (Long) obj[1]      // count (số lượng)
            ));
    }

}
