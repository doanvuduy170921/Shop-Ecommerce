package com.example.ShopEcommerce.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequest {
    private String token;
    private String password;


    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
