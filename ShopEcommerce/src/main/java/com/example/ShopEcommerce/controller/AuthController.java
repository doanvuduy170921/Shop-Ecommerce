package com.example.ShopEcommerce.controller;

import com.example.ShopEcommerce.dto.ForgotPasswordRequest;
import com.example.ShopEcommerce.dto.ResetPasswordRequest;
import com.example.ShopEcommerce.entity.PasswordResetToken;
import com.example.ShopEcommerce.entity.Role;
import com.example.ShopEcommerce.entity.User;
import com.example.ShopEcommerce.entity.VerificationToken;
import com.example.ShopEcommerce.form.LoginForm;
import com.example.ShopEcommerce.form.RegisterForm;
import com.example.ShopEcommerce.repository.PasswordResetTokenRepository;
import com.example.ShopEcommerce.repository.RoleRepository;
import com.example.ShopEcommerce.repository.VerificationTokenRepository;
import com.example.ShopEcommerce.service.AuthService;
import com.example.ShopEcommerce.service.EmailService;
import com.example.ShopEcommerce.service.PasswordResetService;
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

import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthController {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetService passwordResetService;
    private final PasswordResetTokenRepository tokenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public AuthController(AuthService authService, PasswordEncoder passwordEncoder,
                          EmailService emailService, VerificationTokenRepository verificationTokenRepository,
                          PasswordResetService passwordResetService , PasswordResetTokenRepository tokenRepository) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetService = passwordResetService;
        this.tokenRepository = tokenRepository;
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
    public String showForgotPasswordForm() {
        return "Auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        User user = authService.getUserByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "Email không tồn tại!");
            return "redirect:/forgot-password";
        }

        passwordResetService.createPasswordResetToken(user); // Tạo token mới trước

        // Lấy token mới nhất từ DB để gửi email
        Optional<PasswordResetToken> optionalResetToken = tokenRepository.findByUser(user);

        if (optionalResetToken.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Không thể tạo token mới!");
            return "redirect:/forgot-password";
        }

        PasswordResetToken resetToken = optionalResetToken.get();

        if (resetToken == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "Không thể tạo token mới!");
            return "redirect:/forgot-password";
        }

        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken.getToken();
        emailService.sendResetPasswordEmail(user, resetLink);

        redirectAttributes.addFlashAttribute("successMsg", "Email đặt lại mật khẩu đã được gửi!");
        return "redirect:/forgot-password";
    }


    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        System.out.println("🔍 Token từ request: " + token);

        if (token == null || token.isEmpty()) {
            model.addAttribute("errorMsg", "Token không hợp lệ!");
            return "Auth/login";
        }

        PasswordResetToken resetToken = passwordResetService.getToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            System.out.println("Token không hợp lệ hoặc đã hết hạn!");
            model.addAttribute("errorMsg", "Token không hợp lệ hoặc đã hết hạn!");
            return "Auth/login";
        }

        System.out.println("Token hợp lệ: " + resetToken.getToken());

        model.addAttribute("token", token);
        model.addAttribute("resetForm", new ResetPasswordRequest());
        return "Auth/reset-password";
    }


    @PostMapping("/reset-password")
    public String processResetPassword(@ModelAttribute("resetForm") ResetPasswordRequest resetForm,
                                       RedirectAttributes redirectAttributes) {
        String token = resetForm.getToken();
        if (token == null || token.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Token không hợp lệ!");
            return "redirect:/login";
        }

        PasswordResetToken resetToken = passwordResetService.getToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Token không hợp lệ hoặc đã hết hạn!");
            return "redirect:/login";
        }

        User user = resetToken.getUser();
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "Không tìm thấy tài khoản!");
            return "redirect:/login";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(resetForm.getPassword()));
        authService.save(user);
        passwordResetService.deleteToken(resetToken);

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
        user.setIsVerified(false);


        Role defaultRole = roleRepository.findById(1L).orElse(null);
        if (defaultRole != null) {
            user.setRole(defaultRole);
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi hệ thống: Role mặc định không tồn tại.");
            return "redirect:/register";
        }


        authService.save(user);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(user, token);
        redirectAttributes.addFlashAttribute("successMsg", "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận tài khoản.");

        return "redirect:/login";
    }


    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "Token không hợp lệ!");
            return "redirect:/login";
        }

        User user = verificationToken.getUser();
        if (user.getIsVerified()) {
            redirectAttributes.addFlashAttribute("successMsg", "Tài khoản đã được xác nhận trước đó.");
            return "redirect:/login";
        }

        user.setIsVerified(true);
        authService.save(user);
        verificationTokenRepository.delete(verificationToken);

        redirectAttributes.addFlashAttribute("successMsg", "Xác nhận tài khoản thành công! Bạn có thể đăng nhập.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMsg", "Đăng xuất thành công!");
        return "redirect:/";
    }
}
