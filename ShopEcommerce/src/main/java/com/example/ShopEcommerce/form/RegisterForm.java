package com.example.ShopEcommerce.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterForm {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private LocalDate birthDate;

    private String gender;

    private String phoneNumber;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 3, message = "Mật khẩu phải có ít nhất 3 ký tự")
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
}