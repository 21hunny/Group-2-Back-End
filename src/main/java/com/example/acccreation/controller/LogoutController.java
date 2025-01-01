package com.example.acccreation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    /**
     * Logs out the admin by invalidating the session.
     */
    @PostMapping("/admin")
    public ResponseEntity<?> adminLogout(HttpServletRequest request) {
        return handleLogout(request, "Admin");
    }

    /**
     * Logs out the lecturer by invalidating the session.
     */
    @PostMapping("/lecturer")
    public ResponseEntity<?> lecturerLogout(HttpServletRequest request) {
        return handleLogout(request, "Lecturer");
    }

    /**
     * Logs out the student by invalidating the session.
     */
    @PostMapping("/student")
    public ResponseEntity<?> studentLogout(HttpServletRequest request) {
        return handleLogout(request, "Student");
    }

    /**
     * Handles logout for different user roles.
     */
    private ResponseEntity<?> handleLogout(HttpServletRequest request, String role) {
        HttpSession session = request.getSession(false); // Get the existing session, if any
        if (session != null) {
            session.invalidate(); // Destroy the session
        }
        return ResponseEntity.ok(role + " logout successful");
    }
}
