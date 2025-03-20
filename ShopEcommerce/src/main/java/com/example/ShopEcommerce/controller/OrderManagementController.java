
package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.dto.OrderDisplayDTO;
import com.example.ShopEcommerce.entity.Order;
import com.example.ShopEcommerce.entity.OrderDetail;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.service.OrderDetailService;
import com.example.ShopEcommerce.service.OrdersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller 
@RequestMapping("/admin")
public class OrderManagementController {

	private final OrdersService orderService;
	private final OrderDetailService orderDetailService;

	public OrderManagementController(OrdersService orderService, OrderDetailService orderDetailService) {
		this.orderService = orderService;
		this.orderDetailService = orderDetailService;
	}

	@GetMapping("/order-management")
	public String orderManagement(@RequestParam(name = "keyword", required = false) String keyword,
								  @RequestParam(name = "page", defaultValue = "1") int page,
								  @RequestParam(name = "size", defaultValue = "8") int size,
								  Model model,
								  HttpSession session, RedirectAttributes redirectAttributes) {

		User user = (User) session.getAttribute("user");
		if (user != null && user.getRole() != null && user.getRole().getId() == 1) {
			// Xóa hoàn toàn session
			session.removeAttribute("user");
			redirectAttributes.addFlashAttribute("infoMsg", "Bạn đã đăng xuất khỏi tài khoản.");
			return "redirect:/login";
		}
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<OrderDisplayDTO> orderPage = orderService.getOrdersWithProductInfo(keyword, pageable);

		model.addAttribute("orders", orderPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", orderPage.getTotalPages());
		model.addAttribute("keyword", keyword);

		return "management/orderManagement";
	}


	@GetMapping("/order-detail-management/{id}")
	public String orderDetailManagement(@PathVariable Long id, Model model) {
		Order order = orderService.getOrderById(id);
		if (order == null) {
			return "redirect:/admin/order-management";
		}

		OrderDisplayDTO orderDTO = orderService.getOrderDisplayDTOById(id);

		// Lấy thông tin chi tiết đơn hàng
		List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(id);

		model.addAttribute("order", orderDTO);
		model.addAttribute("orderDetails", orderDetails);

		return "management/orderDetail";
	}
}