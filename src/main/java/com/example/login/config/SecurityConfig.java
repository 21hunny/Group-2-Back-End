package com.example.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF protection for login
                .authorizeRequests()
                .requestMatchers("/api/auth/admin/login", "/api/auth/student/login", "/api/auth/lecturer/login")
                .permitAll() // Allow access to login endpoints without authentication
                .anyRequest().authenticated(); // Other endpoints require authentication
        return http.build();  // Return the SecurityFilterChain
    }
}
