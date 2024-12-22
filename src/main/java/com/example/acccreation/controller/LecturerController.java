package com.example.acccreation.controller;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.entity.Lecturer;
import com.example.acccreation.service.AdminService;
import com.example.acccreation.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/lecturer")  // Base path for lecturer-related endpoints
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private AdminService adminService;

    /**
     * POST /api/lecturer/add?adminId=A001
     * Creates a new Lecturer associated with the provided Admin ID.
     *
     * @param adminId          The ID of the Admin creating the Lecturer.
     * @param lecturerRequest  The Lecturer details from the request body.
     * @return The created Lecturer or an error response.
     */
    @PostMapping("/add")
    public ResponseEntity<Lecturer> createLecturer(@RequestParam String adminId,
                                                   @RequestBody Lecturer lecturerRequest) {
        // 1) Fetch the Admin by ID
        Optional<Admin> adminOpt = adminService.findById(adminId);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Admin does not exist
        }
        Admin admin = adminOpt.get();

        // 2) Create the Lecturer
        Lecturer savedLecturer = lecturerService.createLecturer(lecturerRequest, admin);
        return ResponseEntity.ok(savedLecturer);
    }

    // Additional endpoints (e.g., getAll, getById) can be added here
}
