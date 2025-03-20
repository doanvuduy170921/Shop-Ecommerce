package com.example.ShopEcommerce.dto.resp;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResp {
    private Long id;

    private String name;

    private String thumbnail;

    private int price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String description;

    private Double averageRating;

    private int totalRating;

    private CategoryResp category;
}
