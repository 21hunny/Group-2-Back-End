package com.example.acccreation.controller;

import com.example.acccreation.dto.*;
import com.example.acccreation.dto.PasswordUpdateRequest;
import com.example.acccreation.dto.ProgressUpdateResponse;
import com.example.acccreation.entity.Admin;
import com.example.acccreation.entity.Batch;
import com.example.acccreation.entity.Student;
import com.example.acccreation.service.AdminService;
import com.example.acccreation.service.BatchService;
import com.example.acccreation.service.OrganizingTeamService;
import com.example.acccreation.service.StudentService;
import com.example.acccreation.dto.StudentProfileUpdateRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @Autowired
    private OrganizingTeamService organizingTeamService;
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

    /**
     * Update the logged-in student's profile.
     */
    /**
     * Updates the logged-in student's profile.
     */
    @PutMapping("/profile/update")
    public ResponseEntity<?> updateProfile(
            @RequestBody StudentProfileUpdateRequest profileRequest,
            HttpSession session) {
        String studentId = (String) session.getAttribute("userSId");
        String batchId = (String) session.getAttribute("batchId");
        if (studentId == null || batchId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }

        try {
            Student updatedStudent = studentService.updateProfile(studentId, batchId, profileRequest);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Update the logged-in student's password.
     */
    @PutMapping("/profile/password/update")
    public ResponseEntity<?> updatePassword(
            HttpSession session,
            @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        String studentId = (String) session.getAttribute("userSId");
        String batchId = (String) session.getAttribute("batchId");

        if (studentId == null || batchId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }

        try {
            studentService.updatePassword(
                    studentId,
                    batchId,
                    passwordUpdateRequest.getCurrentPassword(),
                    passwordUpdateRequest.getNewPassword()
            );
            return ResponseEntity.ok("Password updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/view-progress")
    public ResponseEntity<?> viewProgressUpdates(HttpSession session) {
        String batchId = (String) session.getAttribute("batchId");

        if (batchId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }

        try {
            List<ProgressUpdateResponse> updates = organizingTeamService.getProgressUpdatesByBatch(batchId);
            return ResponseEntity.ok(updates);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get details of a specific student.
     */
    @GetMapping("/get")
    public ResponseEntity<?> getStudent(HttpSession session) {
        try {
            String studentId = (String) session.getAttribute("userSId");
            Student student = studentService.getStudent(studentId);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all students in a specific batch.
     */
    @GetMapping("/getAll/{batchId}")
    public ResponseEntity<?> getAllStudents(@PathVariable String batchId) {
        try {
            List<Student> students = studentService.getAllStudents(batchId);
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/announcement/batch")
    public ResponseEntity<?> viewAnnouncementsByBatch(HttpSession session) {
        try {
            String batchId = (String) session.getAttribute("batchId");
            if (batchId == null) {
                return ResponseEntity.status(401).body("Student is not logged in or batch not found.");
            }
            List<AnnouncementResponse> announcements = studentService.viewAnnouncementsByBatch(batchId);
            return ResponseEntity.ok(announcements);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/announcement/student")
    public ResponseEntity<?> viewAnnouncementsByStudent(HttpSession session) {
        try {
            String studentId = (String) session.getAttribute("userSId");
            if (studentId == null) {
                return ResponseEntity.status(401).body("Student is not logged in.");
            }
            List<AnnouncementResponse> announcements = studentService.viewAnnouncementsByStudent(studentId);
            return ResponseEntity.ok(announcements);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/workshop/batch")
    public ResponseEntity<?> viewWorkshopsByBatch(HttpSession session) {
        try {
            String batchId = (String) session.getAttribute("batchId");
            if (batchId == null) {
                return ResponseEntity.status(401).body("Student is not logged in or batch not found.");
            }
            List<WorkshopResponse> workshops = studentService.viewWorkshopsByBatch(batchId);
            return ResponseEntity.ok(workshops);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/workshop/student")
    public ResponseEntity<?> viewWorkshopsByStudent(HttpSession session) {
        try {
            String studentId = (String) session.getAttribute("userSId");
            if (studentId == null) {
                return ResponseEntity.status(401).body("Student is not logged in.");
            }
            List<WorkshopResponse> workshops = studentService.viewWorkshopsByStudent(studentId);
            return ResponseEntity.ok(workshops);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/interview/batch")
    public ResponseEntity<?> viewInterviewsByBatch(HttpSession session) {
        try {
            String batchId = (String) session.getAttribute("batchId");
            if (batchId == null) {
                return ResponseEntity.status(401).body("Student is not logged in or batch not found.");
            }
            List<InterviewResponse> interviews = studentService.viewInterviewsByBatch(batchId);
            return ResponseEntity.ok(interviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/interview/student")
    public ResponseEntity<?> viewInterviewsByStudent(HttpSession session) {
        try {
            String studentId = (String) session.getAttribute("userSId");
            if (studentId == null) {
                return ResponseEntity.status(401).body("Student is not logged in.");
            }
            List<InterviewResponse> interviews = studentService.viewInterviewsByStudent(studentId);
            return ResponseEntity.ok(interviews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}
