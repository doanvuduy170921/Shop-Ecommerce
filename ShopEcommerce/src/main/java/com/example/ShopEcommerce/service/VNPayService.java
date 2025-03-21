package com.example.ShopEcommerce.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl}")
    private String vnp_PayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    public String createPaymentUrl(long amount, String orderInfo, String ipAddress) {
        try {
            String vnp_TxnRef = generateTransactionRef();
            String vnp_CreateDate = getCurrentDateTimeFormatted();

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // Số tiền * 100 (VND -> xu)
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", orderInfo);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", ipAddress);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            // Tùy chọn thêm: Thời gian hết hạn thanh toán (15 phút từ lúc tạo)
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // Tạo chuỗi hash dữ liệu và query string
            String queryUrl = createQueryUrl(vnp_Params);

            return vnp_PayUrl + "?" + queryUrl;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo URL thanh toán VNPay", e);
        }
    }

    public Map<String, String> getAllRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            if (paramValue != null && !paramValue.isEmpty()) {
                params.put(paramName, paramValue);
            }
        }

        return params;
    }

    public boolean validatePaymentResponse(Map<String, String> params) {
        try {
            // Lấy secure hash từ response và xóa khỏi params để tính toán lại
            String secureHash = params.get("vnp_SecureHash");
            if (secureHash == null) {
                return false;
            }

            Map<String, String> vnp_Params = new HashMap<>(params);
            vnp_Params.remove("vnp_SecureHash");
            vnp_Params.remove("vnp_SecureHashType");

            // Tính toán lại secure hash để đối chiếu
            String signValue = calculateHash(vnp_Params);

            return signValue.equals(secureHash);
        } catch (Exception e) {
            return false;
        }
    }

    private String createQueryUrl(Map<String, String> params) throws Exception {
        // Sắp xếp tham số theo thứ tự chữ cái
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        // Tạo chuỗi query và chuỗi để tính hash
        StringBuilder query = new StringBuilder();
        StringBuilder hashData = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                // Encode giá trị tham số
                String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());

                // Thêm vào chuỗi query
                query.append(fieldName).append('=').append(encodedValue).append('&');

                // Thêm vào chuỗi tính hash
                hashData.append(fieldName).append('=').append(encodedValue).append('&');
            }
        }

        String queryString = query.substring(0, query.length() - 1);
        String hashString = hashData.substring(0, hashData.length() - 1);

        // Tính toán secure hash
        String secureHash = calculateHash(hashString);

        // Thêm secure hash vào url
        queryString += "&vnp_SecureHash=" + secureHash;

        return queryString;
    }

    private String calculateHash(Map<String, String> params) throws Exception {
        // Sắp xếp tham số theo thứ tự chữ cái
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        // Tạo chuỗi để tính hash
        StringBuilder hashData = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                // Encode giá trị tham số
                String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());

                // Thêm vào chuỗi tính hash
                hashData.append(fieldName).append('=').append(encodedValue).append('&');
            }
        }

        // Xóa ký tự '&' cuối cùng
        String hashString = hashData.substring(0, hashData.length() - 1);

        return hmacSha256(vnp_HashSecret, hashString);
    }

    private String calculateHash(String hashString) throws Exception {
        return hmacSha256(vnp_HashSecret, hashString);
    }

    private String hmacSha256(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] result = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(result);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String generateTransactionRef() {
        return String.valueOf(System.currentTimeMillis()) + new Random().nextInt(1000);
    }

    private String getCurrentDateTimeFormatted() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
