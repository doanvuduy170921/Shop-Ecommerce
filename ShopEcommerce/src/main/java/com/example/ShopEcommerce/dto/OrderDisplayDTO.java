package com.example.ShopEcommerce.dto;

import com.example.ShopEcommerce.entity.Order;
import com.example.ShopEcommerce.entity.OrderDetail;

import java.time.Instant;
import java.util.List;

public class OrderDisplayDTO {
    private Long id;
    private String orderCode;
    private String customerName;
    private Instant orderDate;
    private Float totalMoney;
    private String status;
    private String productPreview; // Ví dụ: "iPhone 13 Pro và 2 sản phẩm khác"
    private int productCount;

    public OrderDisplayDTO(Order order, List<OrderDetail> orderDetails) {
        this.id = order.getId();
        this.orderCode = order.getOrderCode();
        this.customerName = order.getUser() != null ? order.getUser().getName() : "N/A";
        this.orderDate = order.getOrderDate();
        this.totalMoney = order.getTotalMoney();
        this.status = order.getStatus();

        this.productCount = orderDetails.size();

        if (!orderDetails.isEmpty()) {
            OrderDetail firstItem = orderDetails.get(0);
            String firstProductName = firstItem.getProduct() != null ? firstItem.getProduct().getName() : "Không xác định";

            if (productCount > 1) {
                this.productPreview = firstProductName + " và " + (productCount - 1) + " sản phẩm khác";
            } else {
                this.productPreview = firstProductName;
            }
        } else {
            this.productPreview = "Không có sản phẩm";
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductPreview() {
        return productPreview;
    }

    public void setProductPreview(String productPreview) {
        this.productPreview = productPreview;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}