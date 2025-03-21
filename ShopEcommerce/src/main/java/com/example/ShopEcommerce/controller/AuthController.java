package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.entity.Role;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.entity.VerificationToken;
import com.example.ShopEcommerce.form.LoginForm;
import com.example.ShopEcommerce.form.RegisterForm;
import com.example.ShopEcommerce.repository.RoleRepository;
import com.example.ShopEcommerce.repository.VerificationTokenRepository;
import com.example.ShopEcommerce.service.AuthService;
import com.example.ShopEcommerce.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    public AuthController(AuthService authService, PasswordEncoder passwordEncoder,
                          EmailService emailService, VerificationTokenRepository verificationTokenRepository
                          ) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "Auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("loginForm") LoginForm loginForm,
                            BindingResult result, Model model,
                            RedirectAttributes redirectAttributes, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("loginForm", loginForm); // Đảm bảo model có loginForm khi trả về trang login
            return "Auth/login";
        }

        User user = authService.login(loginForm.getEmail(), loginForm.getPassword());
        if (user == null) {
            result.rejectValue("password", "error.password", "Password không chính xác");

            if (authService.getUserByEmail(loginForm.getEmail()) == null) {
                result.rejectValue("email", "error.email", "Email không tồn tại");
            }

            model.addAttribute("loginForm", loginForm); // Đảm bảo model có loginForm
            return "Auth/login";
        }

        if (!user.getIsVerified()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Tài khoản chưa được xác nhận. Vui lòng kiểm tra email.");
            return "redirect:/login";
        }
        if (!user.getIsActive()) {
            redirectAttributes.addFlashAttribute("successMsg", "Đăng nhập thất bại");
            return "redirect:/login";
        }

        Role role = user.getRole();
        if (role == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "Tài khoản chưa được phân quyền.");
            return "redirect:/login";
        }

        session.setAttribute("user", user);
        redirectAttributes.addFlashAttribute("successMsg", "Đăng nhập thành công!");

        return (role.getId() == 2) ? "redirect:/admin/accountManagement" : "redirect:/";
    }


    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "Auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        User user = authService.getUserByEmail(email);
        if (user == null) {
            model.addAttribute("error", "Email không tồn tại!");
            return "Auth/forgot-password";
        }

        // Tạo token reset password
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setTokenExpiryTime(LocalDateTime.now().plusMinutes(30)); // Token hết hạn sau 30 phút
        authService.save(user);
        emailService.sendResetPasswordEmail(user,token);


        model.addAttribute("message", "Hướng dẫn đặt lại mật khẩu đã được gửi đến email!");
        return "Auth/forgot-password";
    }



    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        User user = authService.getUserByResetPasswordToken(token);

        if (user == null || user.getTokenExpiryTime().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn!");
            return "Auth/reset-password";
        }

        model.addAttribute("token", token);
        return "Auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        User user = authService.getUserByResetPasswordToken(token);

        if (user == null || user.getTokenExpiryTime().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Token không hợp lệ hoặc đã hết hạn!");
            return "Auth/forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Mật khẩu xác nhận không khớp");
            return "Auth/reset-password";
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null); // Xóa token
        user.setTokenExpiryTime(null);
        user.setPasswordChangedAt(LocalDateTime.now());
        authService.save(user);

        redirectAttributes.addFlashAttribute("successMsg", "Mật khẩu đã được đặt lại thành công!");
        return "redirect:/login";
    }



    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "Auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerForm") RegisterForm registerForm,
                               BindingResult result, RedirectAttributes redirectAttributes, HttpSession session) {

        System.out.println("email: "+registerForm.getEmail());
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
        user.setIsVerified(true);


        Role defaultRole = roleRepository.findById(1L).orElse(null);
        if (defaultRole != null) {
            user.setRole(defaultRole);
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi hệ thống: Role mặc định không tồn tại.");
            return "redirect:/register";
        }


        authService.save(user);
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiryTime(LocalDateTime.now().plusDays(1));
        authService.save(user);
        emailService.sendVerificationEmail(user, token);
        redirectAttributes.addFlashAttribute("successMsg", "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận tài khoản.");

        return "redirect:/login";
    }


    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        User user = authService.getUserByVerificationToken(token);

        if (user == null || user.getTokenExpiryTime().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("errorMsg", "Token không hợp lệ hoặc đã hết hạn!");
            return "redirect:/login";
        }

        user.setIsVerified(true);
        user.setVerificationToken(null); // Xóa token sau khi xác minh
        user.setTokenExpiryTime(null);
        authService.save(user);

        redirectAttributes.addFlashAttribute("successMsg", "Xác nhận tài khoản thành công! Bạn có thể đăng nhập.");
        return "redirect:/login";
    }


    @GetMapping("/logout")
    public String logoutUser(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMsg", "Đăng xuất thành công!");
        return "redirect:/";
    }

    @GetMapping("")
    public String home() {
        return "redirect:/home";
    }
}
