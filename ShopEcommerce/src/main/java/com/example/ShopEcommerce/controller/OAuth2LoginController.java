package com.example.ShopEcommerce.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.Map;

@Controller
public class OAuth2LoginController {

    @GetMapping("/oauth2-login")
    public String loginPage() {
        return "Auth/login";
    }

    @ModelAttribute("user")
    public Map<String, Object> getUserInfo(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            return ((OAuth2User) authentication.getPrincipal()).getAttributes();
        }
        return Collections.emptyMap();
    }
}
