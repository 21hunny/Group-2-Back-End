package com.example.acccreation.service;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.repository.AdminRepository;
import com.example.acccreation.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Creates a new Admin record with an auto-generated ID like "A001", "A002", etc.
     */
    public Admin createAdmin(Admin admin) {
        // 1) Find current max admin ID (e.g. "A007" or null if none exist)
        String maxId = findMaxAdminId();

        // 2) Generate next ID
        String nextId = CustomIdGenerator.getNextAdminId(maxId);
        admin.setId(nextId);
        admin.setStatus("ACTIVE");

        System.out.println("[DEBUG] Creating admin with ID: " + nextId);
        return adminRepository.save(admin);
    }

    public Optional<Admin> findById(String id) {
        return adminRepository.findById(id);
    }

    /**
     * SELECT MAX(id) FROM admin WHERE id LIKE 'A%'
     * Could return something like 'A007' or null if table empty
     */
    private String findMaxAdminId() {
        String sql = "SELECT MAX(id) FROM admin WHERE id LIKE 'A%'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    // other admin methods...
}


