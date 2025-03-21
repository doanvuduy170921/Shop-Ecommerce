package com.example.ShopEcommerce.repository;

import com.example.ShopEcommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByResetPasswordToken(String token);
    User findByVerificationToken(String verificationToken);
    boolean existsByEmail(String email);
//    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
//    List<User> searchByName(@Param("name") String name);
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
    List<User> findByNameContainingIgnoreCase(String keyword);
    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String keyword, String email,Pageable pageable);
    List<User> findByIsActiveTrue();
}
