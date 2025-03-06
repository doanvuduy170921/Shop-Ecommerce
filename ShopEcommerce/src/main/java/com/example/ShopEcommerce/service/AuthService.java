package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Mã hóa mật khẩu
        userRepository.save(user);
    }


    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void save(User user) {
        userRepository.save(user);
    }
}
