package com.example.acccreation.service;

import com.example.acccreation.config.JwtTokenUtil;
import com.example.acccreation.config.SecurityConfig;
import com.example.acccreation.dto.JwtResponse;
import com.example.acccreation.dto.LoginRequest;
import com.example.acccreation.exception.CustomException;
import com.example.acccreation.entity.Admin;
import com.example.acccreation.entity.Batch;
import com.example.acccreation.entity.Lecturer;
import com.example.acccreation.repository.AdminRepository;
import com.example.acccreation.repository.BatchRepository;
import com.example.acccreation.repository.LecturerRepository;
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

    @Autowired
    private SecurityConfig securityConfig;

    public JwtResponse loginAdmin(LoginRequest request) {
        if (request == null || request.getUserId() == null || request.getPassword() == null) {
            throw new CustomException("User ID and password are required");
        }

        Admin admin = adminRepo.findByUserName(request.getUserId())
                .orElseThrow(() -> new CustomException("Admin not found"));

        if ("BLOCKED".equalsIgnoreCase(admin.getStatus())) {
            throw new CustomException("Your account is blocked due to multiple failed login attempts");
        }

        // Validate hashed password
        if (!securityConfig.passwordEncoder().matches(request.getPassword(), admin.getPassword())) {
            admin.setLoginAttempt(admin.getLoginAttempt() + 1);
            if (admin.getLoginAttempt() >= 3) {
                admin.setStatus("BLOCKED");
            }
            adminRepo.save(admin);
            throw new CustomException("Invalid username or password");
        }

        admin.setLoginAttempt(0);
        adminRepo.save(admin);

        String token = jwtTokenUtil.generateToken(admin.getId());
        HttpSession session = getSession();
        session.setAttribute("userAId", admin.getId());
        session.setAttribute("role", "ADMIN");
        session.setAttribute("token", token);

        return new JwtResponse(token, admin.getId(), "ADMIN");
    }

    public JwtResponse loginStudent(LoginRequest request) {
        if (request == null || request.getUserId() == null || request.getPassword() == null) {
            throw new CustomException("User ID and password are required");
        }

        String username = request.getUserId();
        String inputPassword = request.getPassword();

        List<Batch> batches = batchRepository.findAll();
        if (batches.isEmpty()) {
            throw new CustomException("No batches found");
        }

        for (Batch batch : batches) {
            String tableName = "batch_" + batch.getId();
            String query = "SELECT * FROM " + tableName + " WHERE id = ?";

            try {
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, username);
                if (!rows.isEmpty()) {
                    Map<String, Object> studentData = rows.get(0);
                    String storedPassword = (String) studentData.get("password");

                    // Validate hashed password
                    if (!securityConfig.passwordEncoder().matches(inputPassword, storedPassword)) {
                        throw new CustomException("Invalid username or password");
                    }

                    // Fetch the role of the student
                    String role = (String) studentData.get("role"); // Ensure the "role" column exists in the student table
                    if (role == null || role.isEmpty()) {
                        role = "STUDENT"; // Default role if none is defined
                    }

                    // Generate token and store session attributes
                    String token = jwtTokenUtil.generateToken((String) studentData.get("id"));
                    HttpSession session = getSession();
                    session.setAttribute("userSId", studentData.get("id"));
                    session.setAttribute("batchId", batch.getId());
                    session.setAttribute("role", role);
                    session.setAttribute("token", token);

                    return new JwtResponse(token, (String) studentData.get("id"), role);
                }
            } catch (Exception e) {
                System.err.println("Error querying table " + tableName + ": " + e.getMessage());
            }
        }

        throw new CustomException("Invalid username or password");
    }

    public JwtResponse loginLecturer(LoginRequest request) {
        if (request == null || request.getUserId() == null || request.getPassword() == null) {
            throw new CustomException("User ID and password are required");
        }

        Lecturer lecturer = lecturerRepo.findById(request.getUserId())
                .orElseThrow(() -> new CustomException("Lecturer not found"));

        // Validate hashed password
        if (!securityConfig.passwordEncoder().matches(request.getPassword(), lecturer.getPassword())) {
            throw new CustomException("Invalid username or password");
        }

        String token = jwtTokenUtil.generateToken(lecturer.getId());
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
