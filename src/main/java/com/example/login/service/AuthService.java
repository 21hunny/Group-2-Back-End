package com.example.login.service;

import com.example.login.config.JwtTokenUtil;
import com.example.login.dto.JwtResponse;
import com.example.login.dto.LoginRequest;
import com.example.login.exception.CustomException;
import com.example.login.model.Admin;
import com.example.login.model.Batch;
import com.example.login.model.Lecturer;
import com.example.login.repository.AdminRepository;
import com.example.login.repository.BatchRepository;
import com.example.login.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private LecturerRepository lecturerRepo;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BatchRepository batchRepository;

    // Admin Login
    public JwtResponse loginAdmin(LoginRequest request) {
        String userId = request.getUserId();
        String password = request.getPassword();

        Admin admin = adminRepo.findByUserName(userId)
                .orElseThrow(() -> new CustomException("Admin not found"));

        if (!admin.getPassword().equals(password)) {
            throw new CustomException("Invalid username or password");
        }

        String token = jwtTokenUtil.generateToken(admin.getId());
        return new JwtResponse(token, admin.getId(), "ADMIN");
    }

    // Student Login
    public JwtResponse loginStudent(LoginRequest request) {
        String username = request.getUserId();
        String password = request.getPassword();

        List<Batch> batches = batchRepository.findAll(); // Get all batches

        for (Batch batch : batches) {
            String tableName = "batch_" + batch.getId(); // Construct the table name dynamically

            // Create the SQL query to check the student credentials
            String query = "SELECT * FROM " + tableName + " WHERE SID = ? AND Password = ?";

            try {
                // Execute the query with the given SID (username) and password
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, username, password);

                if (!rows.isEmpty()) {
                    Map<String, Object> studentData = rows.get(0);

                    // Generate JWT token
                    String token = jwtTokenUtil.generateToken((String) studentData.get("SID"));
                    return new JwtResponse(token, (String) studentData.get("SID"), "STUDENT");
                }
            } catch (Exception e) {
                // Log exception details for debugging
                System.err.println("Error querying table " + tableName + ": " + e.getMessage());
                continue; // Move on to the next batch
            }
        }

        // If no student is found in any batch table, throw an exception
        throw new CustomException("Invalid username or password");
    }

    // Lecturer Login
    public JwtResponse loginLecturer(LoginRequest request) {
        String userId = request.getUserId();
        String password = request.getPassword();

        Lecturer lecturer = lecturerRepo.findById(userId)
                .orElseThrow(() -> new CustomException("Lecturer not found"));

        if (!lecturer.getPassword().equals(password)) {
            throw new CustomException("Invalid username or password");
        }

        String token = jwtTokenUtil.generateToken(lecturer.getId());
        return new JwtResponse(token, lecturer.getId(), "LECTURER");
    }
}
