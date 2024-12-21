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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;
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
        if (request == null || request.getUserId() == null || request.getPassword() == null) {
            throw new CustomException("User ID and password are required");
        }

        Admin admin = adminRepo.findByUserName(request.getUserId())
                .orElseThrow(() -> new CustomException("Admin not found"));

        // Check if the admin is blocked
        if ("BLOCKED".equalsIgnoreCase(admin.getStatus())) {
            throw new CustomException("Your account is blocked due to multiple failed login attempts");
        }

        // Validate password
        if (!admin.getPassword().equals(request.getPassword())) {
            // Increment login attempts
            admin.setLoginAttempt(admin.getLoginAttempt() + 1);

            // Check if attempts reach 3
            if (admin.getLoginAttempt() >= 3) {
                admin.setStatus("BLOCKED");
            }

            // Save updated admin to the database
            adminRepo.save(admin);

            throw new CustomException("Invalid username or password");
        }

        // Successful login - Reset login attempts
        admin.setLoginAttempt(0);
        adminRepo.save(admin);

        // Generate JWT token
        String token = jwtTokenUtil.generateToken(admin.getId());

        // Store session details
        HttpSession session = getSession();
        session.setAttribute("userAId", admin.getId());
        session.setAttribute("role", "ADMIN");
        session.setAttribute("token", token);

        return new JwtResponse(token, admin.getId(), "ADMIN");
    }

    // Student Login
    public JwtResponse loginStudent(LoginRequest request) {
        if (request == null || request.getUserId() == null || request.getPassword() == null) {
            throw new CustomException("User ID and password are required");
        }

        String username = request.getUserId();
        String password = request.getPassword();

        List<Batch> batches = batchRepository.findAll(); // Get all batches
        if (batches.isEmpty()) {
            throw new CustomException("No batches found");
        }

        for (Batch batch : batches) {
            String tableName = "batch_" + batch.getId(); // Construct the table name dynamically

            String query = "SELECT * FROM " + tableName + " WHERE SID = ? AND Password = ?";
            try {
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, username, password);

                if (!rows.isEmpty()) {
                    Map<String, Object> studentData = rows.get(0);

                    // Generate JWT token
                    String token = jwtTokenUtil.generateToken((String) studentData.get("SID"));

                    // Store session details
                    HttpSession session = getSession();
                    session.setAttribute("userSId", studentData.get("SID"));
                    session.setAttribute("role", "STUDENT");
                    session.setAttribute("token", token);

                    return new JwtResponse(token, (String) studentData.get("SID"), "STUDENT");
                }
            } catch (Exception e) {
                System.err.println("Error querying table " + tableName + ": " + e.getMessage());
            }
        }

        throw new CustomException("Invalid username or password");
    }

    // Lecturer Login
    public JwtResponse loginLecturer(LoginRequest request) {
        if (request == null || request.getUserId() == null || request.getPassword() == null) {
            throw new CustomException("User ID and password are required");
        }

        Lecturer lecturer = lecturerRepo.findById(request.getUserId())
                .orElseThrow(() -> new CustomException("Lecturer not found"));

        if (!lecturer.getPassword().equals(request.getPassword())) {
            throw new CustomException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtTokenUtil.generateToken(lecturer.getId());

        // Store session details
        HttpSession session = getSession();
        session.setAttribute("userLId", lecturer.getId());
        session.setAttribute("role", "LECTURER");
        session.setAttribute("token", token);

        return new JwtResponse(token, lecturer.getId(), "LECTURER");
    }

    // Utility method to get the current HTTP session
    private HttpSession getSession() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attr.getRequest().getSession(true); // true = create a new session if one doesn't exist
        } catch (Exception e) {
            throw new CustomException("Unable to retrieve session: " + e.getMessage());
        }
    }
}
