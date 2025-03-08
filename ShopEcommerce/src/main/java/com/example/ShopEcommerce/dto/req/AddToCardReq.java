package com.example.ShopEcommerce.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCardReq {
    private Long userId;
    private Integer productId;
    private Integer quantity;
}
