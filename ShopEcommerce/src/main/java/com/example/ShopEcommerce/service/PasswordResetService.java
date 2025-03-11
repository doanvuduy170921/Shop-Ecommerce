package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.entity.PasswordResetToken;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.repository.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Transactional
    public String createPasswordResetToken(User user) {
        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        // Lấy thời gian hệ thống theo múi giờ Asia/Ho_Chi_Minh
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        // Tạo thời gian hết hạn, nhưng lưu dưới dạng UTC
        ZonedDateTime expiryDateUtc = now.plusHours(1).withZoneSameInstant(ZoneId.of("UTC"));

        System.out.println("Tạo token mới: " + token);
        System.out.println("Thời gian hệ thống (Asia/Ho_Chi_Minh): " + now);
        System.out.println("Token hết hạn vào (UTC): " + expiryDateUtc);

        PasswordResetToken resetToken = new PasswordResetToken(user, token, expiryDateUtc.toLocalDateTime());
        tokenRepository.save(resetToken);

        return token;
    }

    public PasswordResetToken getToken(String token) {
        System.out.println("Token từ request: " + token);

        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null) {
            System.out.println("Token không tồn tại trong DB!");
            return null;
        }

        // Lấy thời gian hiện tại theo UTC để so sánh với dữ liệu trong DB
        LocalDateTime nowUtc = LocalDateTime.now(ZoneOffset.UTC);

        if (resetToken.getExpiryDate().isBefore(nowUtc)) {
            System.out.println("Token đã hết hạn!");
            return null;
        }

        System.out.println("Token hợp lệ: " + resetToken.getToken());
        return resetToken;
    }

    @Transactional
    public void deleteToken(PasswordResetToken token) {
        if (token != null) {
            tokenRepository.delete(token);
        }
    }
}
