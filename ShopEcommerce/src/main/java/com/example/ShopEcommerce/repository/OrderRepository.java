package com.example.ShopEcommerce.repository;

import com.example.ShopEcommerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByOrderCodeContainingIgnoreCase(String orderCode, Pageable pageable);

    // Search by user name
    @Query("SELECT o FROM Order o JOIN o.user u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :userName, '%'))")
    Page<Order> findByUserNameContainingIgnoreCase(@Param("userName") String userName, Pageable pageable);

    // Search by both order code or user name
    @Query("SELECT o FROM Order o JOIN o.user u WHERE LOWER(o.orderCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Order> findByOrderCodeOrUserNameContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.user u JOIN OrderDetail od ON od.order.id = o.id JOIN od.product p " +
            "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Order> findByUserNameOrProductNameContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    // Get all orders with pagination
    Page<Order> findAll(Pageable pageable);

}
