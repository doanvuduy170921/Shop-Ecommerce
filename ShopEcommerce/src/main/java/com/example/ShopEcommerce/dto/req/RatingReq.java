package com.example.ShopEcommerce.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingReq {
    private Long productId;
    @NotNull
    private int rating;
    private String review;
}
