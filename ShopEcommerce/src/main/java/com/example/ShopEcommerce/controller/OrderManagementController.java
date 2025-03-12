
package com.example.ShopEcommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller 
@RequestMapping("/admin")
public class OrderManagementController {
	@GetMapping("/order-management")
	public String orderManagement() {
		return "management/orderManagement"; // Không cần .html
	}
	@GetMapping("/order-detail-management")
	public String orderDetailManagement() {
		return "management/orderDetail"; // Không cần .html
	}

}