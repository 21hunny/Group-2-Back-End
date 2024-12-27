package com.example.acccreation.controller;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.entity.Lecturer;
import com.example.acccreation.repository.AdminRepository;
import com.example.acccreation.service.LecturerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;



@RestController
@RequestMapping("/api/lecturer")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private AdminRepository adminRepository;

    /**
     * POST /api/lecturer/add
     * Creates a new Lecturer associated with the logged-in Admin.
     */
    @PostMapping("/add")
    public ResponseEntity<Lecturer> createLecturer(@RequestBody Lecturer lecturerRequest, HttpSession session) {
        String adminId = (String) session.getAttribute("userAId");
        if (adminId == null || adminId.isEmpty()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        Optional<Admin> adminOpt = adminRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        Admin admin = adminOpt.get();
        Lecturer savedLecturer = lecturerService.createLecturer(lecturerRequest, admin);
        return ResponseEntity.ok(savedLecturer);
    }

    /**
     * PUT /api/lecturer/update/{lecturerId}
     * Updates an existing Lecturer by ID.
     */
    @PutMapping("/update/{lecturerId}")
    public ResponseEntity<?> updateLecturer(@PathVariable String lecturerId, @RequestBody Lecturer lecturerRequest) {
        try {
            Lecturer updatedLecturer = lecturerService.updateLecturer(lecturerId, lecturerRequest);
            return ResponseEntity.ok(updatedLecturer);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    /**
     * DELETE /api/lecturer/delete/{lecturerId}
     * Deletes a Lecturer by ID.
     */
    @DeleteMapping("/delete/{lecturerId}")
    public ResponseEntity<?> deleteLecturer(@PathVariable String lecturerId) {
        try {
            lecturerService.deleteLecturer(lecturerId);
            return ResponseEntity.ok("Lecturer deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Lecturer not found with ID: " + lecturerId);
        }
    }
}
