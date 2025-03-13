package com.example.ShopEcommerce.service;

import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Transactional
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với ID: " + id));
        if (!user.getIsActive()) {
            user.setIsActive(true);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        } else {
            user.setIsActive(false);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }



    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
    public void updateUser(User user) {
        userRepository.save(user);
    }
    public Page<User> searchUsersPaginated(String keyword,String email, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword,email, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public boolean toggleUserStatus(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsActive(!user.getIsActive());
            userRepository.save(user);
            return true;
        }
        return false;
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public List<User> findAllActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    // Trong UserService
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
