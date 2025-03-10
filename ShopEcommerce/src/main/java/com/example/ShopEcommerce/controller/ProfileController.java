package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/infoAccount/{id}")
    public String infoAccount(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/infoAccount";
        }
        model.addAttribute("user", user);
        return "management/infoAccount";
    }

    @GetMapping("/updateAccount/{id}")
    public String updateAccount(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/updateAccount";
        }
        model.addAttribute("user", user);
        return "management/updateAccount";
    }

    @PostMapping("/updateAccount/{id}")
    public String updateAccount(
            @PathVariable Long id,
            @ModelAttribute("user") User updatedUser,
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            RedirectAttributes redirectAttributes) {

        User existingUser = userService.findById(id);
        if (existingUser == null) {
            redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại!");
            return "redirect:/updateAccount/" + id;
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setEmail(updatedUser.getEmail());

        if (newPassword != null && !newPassword.isEmpty()) {
            if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
                return "redirect:/updateAccount/" + id;
            }
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Xác nhận mật khẩu không khớp!");
                return "redirect:/updateAccount/" + id;
            }
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        userService.updateUser(existingUser);

        redirectAttributes.addFlashAttribute("success", "Cập nhật tài khoản thành công!");
        return "redirect:/updateAccount/" + id;
    }
}

