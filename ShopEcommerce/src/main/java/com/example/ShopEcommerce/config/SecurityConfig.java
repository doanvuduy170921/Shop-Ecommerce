//package com.example.ShopEcommerce.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//	// mã hóa
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	// cấu hình bảo mật
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		//CSRF (Cross-Site Request Forgery) giúp bảo vệ ứng dụng khỏi các cuộc tấn công giả mạo.
//	    http.csrf(csrf -> csrf.disable()) // Tắt CSRF khi test API trên Postman
//	        .authorizeHttpRequests(auth -> auth
//	            .requestMatchers("/", "/home", "/register", "/css/**", "/js/**","/api/carts/**").permitAll() // Cho phép truy cập công khai
//	            .requestMatchers("/cart/**").permitAll()
//	            .anyRequest().authenticated() // Các trang khác yêu cầu đăng nhập
//	        )
//	        
//	        // sau khi đăng nhập thành công thì chuyển đến trang ...
//	        .formLogin(form -> form
//	            .loginPage("/login")
//	            .defaultSuccessUrl("/api/order-management", false)
//	            .permitAll()
//	        )
//	        
//	        // sau khi đăng xuất thì chuyển đến trang...
//	        .logout(logout -> logout
//	            .logoutSuccessUrl("/home")
//	            .permitAll()
//	        );
//
//	    return http.build();
//	}
//
//
//	 
//	// để bảo vệ tài khoản, tạo tài khoản mặc định
//	@Bean
//	public UserDetailsService userDetailsService() {
//	    UserDetails user = User.withUsername("admin")
//	            .password(passwordEncoder().encode("admin")) // Mã hóa mật khẩu
//	            .roles("ADMIN")
//	            .build();
//
//	    return new InMemoryUserDetailsManager(user);
//	}
//
////	@Bean
////	public UserDetailsService userDetailsService() {
////		UserDetails user = User
////				.username("admin")
////				.password("admin")
////				.roles("ADMIN")
////				.build();
////		return new InMemoryUserDetailsManager(user);
////	}
//
//	/*
//	 * @Bean public UserDetailsService userDetailsService() { UserDetails user =
//	 * User.withUsername("admin@gmail.com") // Dùng email thay vì username
//	 * .password(passwordEncoder().encode("admin")) // Mật khẩu đã mã hóa
//	 * .roles("ADMIN").build();
//	 * 
//	 * return new InMemoryUserDetailsManager(user); }
//	 */
//}
