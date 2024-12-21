package com.example.login.controller;

import com.example.login.dto.JwtResponse;
import com.example.login.dto.LoginRequest;
import com.example.login.exception.CustomException;
import com.example.login.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest request) {
        try {
            JwtResponse jwtResponse = authService.loginAdmin(request);
            return ResponseEntity.ok(jwtResponse);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/student/login")
    public ResponseEntity<?> studentLogin(@RequestBody LoginRequest request) {
        try {
            JwtResponse jwtResponse = authService.loginStudent(request);
            return ResponseEntity.ok(jwtResponse);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/lecturer/login")
    public ResponseEntity<?> lecturerLogin(@RequestBody LoginRequest request) {
        try {
            JwtResponse jwtResponse = authService.loginLecturer(request);
            return ResponseEntity.ok(jwtResponse);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
