package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(User user, String token) {
        String subject = "Xác nhận tài khoản - Shop Ecommerce";
        String verificationUrl = "http://localhost:8080/verify?token=" + token;

        String emailContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 20px auto; border: 1px solid #ddd; border-radius: 10px; padding: 20px; background-color: #f9f9f9;'>"
                + "<h2 style='text-align: center; color: #333;'>Xác nhận tài khoản của bạn</h2>"
                + "<p>Chào <strong>" + user.getName() + "</strong>,</p>"
                + "<p>Bạn đã đăng ký tài khoản trên <strong>Shop Ecommerce</strong>. Để kích hoạt tài khoản, vui lòng nhấn vào nút bên dưới:</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<a href='" + verificationUrl + "' style='background-color: #28a745; color: white; text-decoration: none; padding: 10px 20px; border-radius: 5px; font-size: 16px;'>Xác nhận tài khoản</a>"
                + "</div>"
                + "<p>Nếu bạn không đăng ký tài khoản, vui lòng bỏ qua email này.</p>"
                + "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'/>"
                + "<p style='text-align: center; font-size: 14px; color: #555;'>© 2025 Shop Ecommerce. All rights reserved.</p>"
                + "</div>";

        sendEmail(user.getEmail(), subject, emailContent);
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // Gửi email dưới dạng HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendResetPasswordEmail(User user, String token) {
        String subject = "Đặt lại mật khẩu - Shop Ecommerce";
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;

        String emailContent = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 20px auto; border: 1px solid #ddd; border-radius: 10px; padding: 20px; background-color: #f9f9f9;'>"
                + "<h2 style='text-align: center; color: #333;'>Yêu cầu đặt lại mật khẩu</h2>"
                + "<p>Chào <strong>" + user.getName() + "</strong>,</p>"
                + "<p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>"
                + "<p>Nhấn vào nút bên dưới để đặt lại mật khẩu:</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<a href='" + resetUrl + "' style='background-color: #007bff; color: white; text-decoration: none; padding: 10px 20px; border-radius: 5px; font-size: 16px;'>Đặt lại mật khẩu</a>"
                + "</div>"
                + "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, hãy bỏ qua email này.</p>"
                + "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'/>"
                + "<p style='text-align: center; font-size: 14px; color: #555;'>© 2025 Shop Ecommerce. All rights reserved.</p>"
                + "</div>";

        sendEmail(user.getEmail(), subject, emailContent);
    }

}
