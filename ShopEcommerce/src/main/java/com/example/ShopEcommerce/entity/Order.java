package com.example.ShopEcommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "status")
    private String status;

    @Column(name = "total_money")
    private Float totalMoney;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_code")
    private String orderCode;

}