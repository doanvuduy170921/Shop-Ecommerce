package com.example.ShopEcommerce;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "12345";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Mật khẩu đã mã hóa: " + encodedPassword);
    }
}
