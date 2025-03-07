package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> searchUsers(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findByNameContainingIgnoreCase(keyword);
//            return userRepository.searchByName(keyword);
        }
        return userRepository.findAll();
    }
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
