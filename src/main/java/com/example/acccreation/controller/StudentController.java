package com.example.acccreation.controller;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.entity.Batch;
import com.example.acccreation.entity.Student;
import com.example.acccreation.service.AdminService;
import com.example.acccreation.service.BatchService;
import com.example.acccreation.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private AdminService adminService;

    /**
     * Creates a new student in the dynamic table batch_<batchId>.
     */
    @PostMapping("/add/{batchId}")
    public ResponseEntity<?> createStudent(@PathVariable String batchId,
                                           HttpSession session,
                                           @RequestBody Student studentRequest) {
        String adminId = (String) session.getAttribute("userAId");
        if (adminId == null) {
            return ResponseEntity.badRequest().body("Admin ID not found in session.");
        }

        Optional<Admin> adminOpt = adminService.findById(adminId);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Admin not found.");
        }
        Admin admin = adminOpt.get();

        Batch batch = batchService.getBatchById(batchId);
        if (batch == null) {
            return ResponseEntity.badRequest().body("Batch not found.");
        }

        try {
            Student newStudent = studentService.createStudent(studentRequest, admin, batch);
            return ResponseEntity.ok(newStudent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error while creating student: " + e.getMessage());
        }
    }

    /**
     * Updates an existing student.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable("id") String studentId,
                                           @RequestBody Student studentRequest) {
        try {
            Student updatedStudent = studentService.updateStudent(studentId, studentRequest);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while updating student: " + e.getMessage());
        }
    }

    /**
     * Deletes a student by ID.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") String studentId) {
        try {
            studentService.deleteStudent(studentId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while deleting student: " + e.getMessage());
        }
    }
}
