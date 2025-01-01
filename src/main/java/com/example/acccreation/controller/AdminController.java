package com.example.acccreation.controller;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Create a new Admin.
     */
    @PostMapping("/add")
    public ResponseEntity<?> createAdmin(@RequestBody Admin adminRequest) {
        try {
            Admin admin = adminService.createAdmin(adminRequest);
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error while creating admin: " + e.getMessage());
        }
    }

    /**
     * Get an Admin by ID.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAdmin(@PathVariable String id) {
        try {
            Optional<Admin> adminOpt = adminService.findById(id);
            return adminOpt.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error while retrieving admin: " + e.getMessage());
        }
    }

    /**
     * Update an Admin by ID.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable String id, @RequestBody Admin adminRequest) {
        try {
            Optional<Admin> adminOpt = adminService.findById(id);
            if (adminOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Admin updatedAdmin = adminService.updateAdmin(id, adminRequest);
            return ResponseEntity.ok(updatedAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error while updating admin: " + e.getMessage());
        }
    }

    /**
     * Delete an Admin by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable String id) {
        try {
            boolean isDeleted = adminService.deleteAdmin(id);
            if (!isDeleted) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error while deleting admin: " + e.getMessage());
        }
    }

    /**
     * Toggle Admin status.
     */
    @PutMapping("/toggleStatus/{id}")
    public ResponseEntity<?> toggleAdminStatus(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        try {
            String status = requestBody.get("status");
            if (!"ACTIVE".equalsIgnoreCase(status) && !"BLOCKED".equalsIgnoreCase(status)) {
                return ResponseEntity.badRequest().body("Invalid status value");
            }
            Optional<Admin> adminOpt = adminService.findById(id);
            if (adminOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Admin updatedAdmin = adminService.toggleAdminStatus(id, status.toUpperCase());
            return ResponseEntity.ok(updatedAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error while toggling admin status: " + e.getMessage());
        }
    }

}
