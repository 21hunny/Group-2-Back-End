package com.example.acccreation.controller;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Create new Admin
    @PostMapping("admin/add")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin adminRequest) {
        Admin admin = adminService.createAdmin(adminRequest);
        return ResponseEntity.ok(admin);
    }

    // Get Admin by ID
    @GetMapping("admin/get{id}")
    public ResponseEntity<Admin> getAdmin(@PathVariable String id) {
        Optional<Admin> adminOpt = adminService.findById(id);
        return adminOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
