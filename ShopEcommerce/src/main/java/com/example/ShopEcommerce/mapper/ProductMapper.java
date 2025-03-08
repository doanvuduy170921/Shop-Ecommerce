package com.example.ShopEcommerce.mapper;

import com.example.ShopEcommerce.dto.resp.ProductResp;
import com.example.ShopEcommerce.entity.Product;

public class ProductMapper {
    public static ProductResp toProductResp(Product product) {
        ProductResp productResp = new ProductResp();
        productResp.setId(product.getId());
        productResp.setName(product.getName());
        productResp.setPrice(product.getPrice());
        productResp.setDescription(product.getDescription());
        productResp.setThumbnail(product.getThumbnail());
        productResp.setCategory(CategoryMapper.toCategoryResp(product.getCategory()));
        return productResp;
    }
}
