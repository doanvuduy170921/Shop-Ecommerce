package com.example.ShopEcommerce.repository;

import com.example.ShopEcommerce.entity.PasswordResetToken;
import com.example.ShopEcommerce.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUser(User user);
    PasswordResetToken findByToken(String token);
    void deleteByUser(User user);
}


