package com.example.ShopEcommerce.dto.req;

import com.example.ShopEcommerce.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResp {
    private Long id;
    private String name;
    private int price;
    private String thumbnail;
    private String description;


    public ProductResp(Product product) {
        if (product != null) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.thumbnail = product.getThumbnail();
            this.description = product.getDescription();
        }
    }
}

