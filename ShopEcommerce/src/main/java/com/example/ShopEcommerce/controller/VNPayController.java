package com.example.ShopEcommerce.controller;



import com.example.ShopEcommerce.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.IOException;
import java.util.Map;

@Controller
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/payment")
    public String showPaymentPage() {
        return "payment"; // Trả về file payment.html trong thư mục templates
    }

    @PostMapping("/create-payment")
    public void createPayment(
            @RequestParam("amount") long amount,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String ipAddress = getClientIpAddress(request);
        String paymentUrl = vnPayService.createPaymentUrl(amount, orderInfo, ipAddress);
        response.sendRedirect(paymentUrl);
    }

    @GetMapping("/vnpay-return")
    public String vnpayReturn(HttpServletRequest request, Model model) {
        Map<String, String> vnpParams = vnPayService.getAllRequestParams(request);

        // Xác thực chữ ký từ VNPay
        if (vnPayService.validatePaymentResponse(vnpParams)) {
            String responseCode = vnpParams.get("vnp_ResponseCode");

            if ("00".equals(responseCode)) {
                // Thanh toán thành công
                model.addAttribute("success", true);
                model.addAttribute("message", "Thanh toán thành công!");
                model.addAttribute("transactionId", vnpParams.get("vnp_TransactionNo"));
                model.addAttribute("orderInfo", vnpParams.get("vnp_OrderInfo"));
                model.addAttribute("amount", Long.parseLong(vnpParams.get("vnp_Amount")) / 100);
            } else {
                // Thanh toán thất bại
                model.addAttribute("success", false);
                model.addAttribute("message", "Thanh toán thất bại, mã lỗi: " + responseCode);
            }
        } else {
            // Chữ ký không hợp lệ
            model.addAttribute("success", false);
            model.addAttribute("message", "Thông tin thanh toán không hợp lệ!");
        }

        return "payment-result";
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // Nếu là IPv6 loopback
        if (ipAddress.equals("0:0:0:0:0:0:0:1")) {
            ipAddress = "127.0.0.1";
        }
        return ipAddress;
    }
}