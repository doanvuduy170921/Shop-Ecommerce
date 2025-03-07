package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.Role;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.form.LoginForm;
import com.example.ShopEcommerce.form.RegisterForm;
import com.example.ShopEcommerce.repository.RoleRepository;
import com.example.ShopEcommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository; // Repository để lấy role từ database

    @Autowired
    public AuthController(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "Auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                            BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
        if (result.hasErrors()) {
            return "Auth/login";
        }

        User user = authService.login(loginForm.getEmail(), loginForm.getPassword());
        if (user == null) {
            result.rejectValue("password", "error.password", "Password không chính xác");

            if (authService.getUserByEmail(loginForm.getEmail()) == null) {
                result.rejectValue("email", "error.email", "Email không tồn tại");
            }
            return "Auth/login";
        }

        // Gán role cho user
        if ("admin@gmail.com".equalsIgnoreCase(user.getEmail())) {
            Role adminRole = roleRepository.findByName("ADMIN");
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setName("ADMIN");
                roleRepository.save(adminRole);
            }
            user.setRole(adminRole);
        } else {
            Role userRole = roleRepository.findByName("USER");
            if (userRole == null) {
                userRole = new Role();
                userRole.setName("USER");
                roleRepository.save(userRole);
            }
            user.setRole(userRole);
        }

        session.setAttribute("user", user);
        redirectAttributes.addFlashAttribute("successMsg", "Đăng nhập thành công!");

        // Chuyển hướng theo role
        if ("ADMIN".equals(user.getRole().getName())) {
            return "redirect:cart/carts"; // Kiểm tra đúng đường dẫn của bạn
        } else {
            return "redirect:/";
        }
    }





    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "Auth/register";
    }


    @PostMapping("/register/1")
    public String registerUser(@ModelAttribute User user) {
        authService.registerUser(user);
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerForm") RegisterForm registerForm,
                               BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {
        if (result.hasErrors()) {
            return "Auth/register";
        }

        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Mật khẩu xác nhận không khớp");
            redirectAttributes.addFlashAttribute("errorMsg", "Mật khẩu xác nhận không khớp!");
            return "redirect:/register";
        }

        if (authService.getUserByEmail(registerForm.getEmail()) != null) {
            result.rejectValue("email", "error.email", "Email đã tồn tại");
            redirectAttributes.addFlashAttribute("errorMsg", "Email đã tồn tại. Vui lòng sử dụng email khác!");
            return "redirect:/register";
        }


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setEmail(registerForm.getEmail());
        user.setName(registerForm.getFullName());
        user.setDateOfBirth(registerForm.getBirthDate());
        user.setPassword(encoder.encode(registerForm.getPassword()));

        authService.save(user);
        session.setAttribute("user", user);
        redirectAttributes.addFlashAttribute("successMsg", "Đăng ký thành công! Bạn có thể đăng nhập ngay.");
        return "redirect:/login";

    }


    @GetMapping("/logout")
    public String logoutUser(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMsg", "Đăng xuất thành công!");
        return "redirect:/";
    }
}
