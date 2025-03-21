package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.Product;
import com.example.ShopEcommerce.entity.ProductImage;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @GetMapping("/infoAccount/{id}")
//    public String infoAccount(@PathVariable Long id, Model model, HttpSession session){
//        User user = userService.findById(id);
//        if (user == null) {
//            return "redirect:/infoAccount";
//        }
//        model.addAttribute("user", user);
//        return "management/infoAccount";
//    }
//
//    @GetMapping("/updateAccount/{id}")
//    public String updateAccount(@PathVariable Long id, Model model,HttpSession session){
//        User user = userService.findById(id);
//        if (user == null) {
//            return "redirect:/updateAccount";
//        }
//        model.addAttribute("user", user);
//        return "management/updateAccount";
//    }
//
//    @PostMapping("/updateAccount/{id}")
//    public String updateAccount(
//            @PathVariable Long id,
//            @ModelAttribute("user") User updatedUser,
//            @RequestParam(value = "oldPassword", required = false) String oldPassword,
//            @RequestParam(value = "newPassword", required = false) String newPassword,
//            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
//            RedirectAttributes redirectAttributes,
//            HttpSession session) {
//
//        User existingUser = userService.findById(id);
//        if (existingUser == null) {
//            redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại!");
//            return "redirect:/updateAccount/" + id;
//        }
//
//        existingUser.setName(updatedUser.getName());
//        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
//        existingUser.setAddress(updatedUser.getAddress());
//        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
//        existingUser.setEmail(updatedUser.getEmail());
//
//        if (newPassword != null && !newPassword.isEmpty()) {
//            if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
//                redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
//                return "redirect:/updateAccount/" + id;
//            }
//            if (!newPassword.equals(confirmPassword)) {
//                redirectAttributes.addFlashAttribute("error", "Xác nhận mật khẩu không khớp!");
//                return "redirect:/updateAccount/" + id;
//            }
//            existingUser.setPassword(passwordEncoder.encode(newPassword));
//        }
//
//        userService.updateUser(existingUser);
//
//        redirectAttributes.addFlashAttribute("success", "Cập nhật tài khoản thành công!");
//        return "redirect:/infoAccount/" + id;
//    }

    @GetMapping("/infoAccount")
    public String infoAccount( Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User user = userService.findById(loggedInUser.getId());
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "management/infoAccount";
    }

    @GetMapping("/updateAccount")
    public String updateAccount(Model model,HttpSession session){
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User user = userService.findById(loggedInUser.getId());
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "management/updateAccount";
    }

    @PostMapping("/updateAccount")
    public String updateAccount(
            @ModelAttribute("user") User updatedUser,
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User existingUser = userService.findById(loggedInUser.getId());
        if (existingUser == null) {
            redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại!");
            return "redirect:/updateAccount";
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setEmail(updatedUser.getEmail());

        if (newPassword != null && !newPassword.isEmpty()) {
            if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
                return "redirect:/updateAccount";
            }
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Xác nhận mật khẩu không khớp!");
                return "redirect:/updateAccount";
            }
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        userService.updateUser(existingUser);

        redirectAttributes.addFlashAttribute("success", "Cập nhật tài khoản thành công!");
        return "redirect:/infoAccount";
    }

    @PostMapping("/infoAccount/deactivate")
    public String deactivateUser(RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            User loggedInUser = (User) session.getAttribute("user");
            if (loggedInUser == null) {
                return "redirect:/login";
            }
            userService.activateUser(loggedInUser.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Tài khoản đã xóa thành công");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa tài khoản: " + e.getMessage());
            return "redirect:/infoAccount";
        }
    }
}

