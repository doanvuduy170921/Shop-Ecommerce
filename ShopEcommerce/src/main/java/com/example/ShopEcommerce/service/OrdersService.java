package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.dto.OrderDisplayDTO;
import com.example.ShopEcommerce.entity.Order;
import com.example.ShopEcommerce.entity.OrderDetail;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersService {
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;

    public OrdersService(OrderRepository orderRepository, OrderDetailService orderDetailService) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> searchOrders(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllOrders(pageable);
        }
        return orderRepository.findByUserNameOrProductNameContainingIgnoreCase(keyword, pageable);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Page<OrderDisplayDTO> getOrdersWithProductInfo(String keyword, Pageable pageable) {
        // Lấy danh sách đơn hàng
        Page<Order> orderPage = searchOrders(keyword, pageable);
        List<Order> orders = orderPage.getContent();

        // Chuyển đổi sang DTO với thông tin sản phẩm
        List<OrderDisplayDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(order.getId());
            orderDTOs.add(new OrderDisplayDTO(order, orderDetails));
        }

        // Trả về Page với các DTO
        return new PageImpl<>(orderDTOs, pageable, orderPage.getTotalElements());
    }
    public OrderDisplayDTO getOrderDisplayDTOById(Long id) {
        Order order = getOrderById(id);
        if (order == null) {
            return null;
        }
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(order.getId());
        return new OrderDisplayDTO(order, orderDetails);
    }
}
