package com.example.acccreation.controller;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.entity.Batch;
import com.example.acccreation.entity.Student;
import com.example.acccreation.service.AdminService;
import com.example.acccreation.service.BatchService;
import com.example.acccreation.service.StudentService;
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
     *
     * Now we pass both 'batchId' and 'adminId' as query params, e.g.:
     * POST /api/student/add?batchId=B2024&adminId=A001
     */
    @PostMapping("/add")
    public ResponseEntity<Student> createStudent(@RequestParam String batchId,
                                                 @RequestParam String adminId,    // <--- added
                                                 @RequestBody Student studentRequest)
    {
        // 1) Find the Admin
        Optional<Admin> adminOpt = adminService.findById(adminId);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.badRequest().build(); // or 404
        }
        Admin admin = adminOpt.get();

        // 2) Find the Batch
        Batch batch = batchService.getBatchById(batchId);
        if (batch == null) {
            return ResponseEntity.notFound().build();
        }

        // 3) Create the student in batch_<batchId>
        Student newStudent = studentService.createStudent(studentRequest, admin, batch);
        return ResponseEntity.ok(newStudent);
    }
}
