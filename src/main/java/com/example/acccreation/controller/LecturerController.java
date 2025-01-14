package com.example.acccreation.controller;

import com.example.acccreation.dto.*;
import com.example.acccreation.entity.*;
import com.example.acccreation.repository.AdminRepository;
import com.example.acccreation.service.LecturerService;
import com.example.acccreation.service.OrganizingTeamService;
import com.example.acccreation.service.PortfolioService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/lecturer")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private PortfolioService portfolioService;

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

    @GetMapping("/profile")
    public ResponseEntity<?> viewProfile(HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }

        try {
            Lecturer lecturer = lecturerService.getLecturerById(lecturerId);
            return ResponseEntity.ok(lecturer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

    /**
     * Update Lecturer Profile
     * PUT /api/lecturer/profile/update
     */
    @PutMapping("/profile/update")
    public ResponseEntity<?> updateProfile(HttpSession session, @RequestBody Lecturer lecturerRequest) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }

        try {
            Lecturer updatedLecturer = lecturerService.updateLecturerProfile(lecturerId, lecturerRequest);
            return ResponseEntity.ok(updatedLecturer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

    /**
     * Update Lecturer Password
     * PUT /api/lecturer/password/update
     */
    @PutMapping("/password/update")
    public ResponseEntity<?> updatePassword(HttpSession session, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }

        try {
            Lecturer updatedLecturer = lecturerService.updateLecturerPassword(lecturerId, passwordUpdateRequest);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all batches
     * Endpoint: GET /api/lecturer/batches
     */
    @GetMapping("get/batches")
    public ResponseEntity<?> getAllBatches() {
        try {
            List<String> batchIds = lecturerService.getAllBatchIds();
            return ResponseEntity.ok(batchIds);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching batches: " + e.getMessage());
        }
    }

    /**
     * Get all students of a batch
     * Endpoint: GET /api/lecturer/students/{batchId}
     */
    @GetMapping("search/students/{batchId}")
    public ResponseEntity<?> getStudentsByBatch(@PathVariable String batchId) {
        try {
            List<String> studentIds = lecturerService.getStudentsByBatch(batchId);
            return ResponseEntity.ok(studentIds);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

    /**
     * Add an individual Announcement
     * Endpoint: POST /api/lecturer/announcement/add/individual/{studentId}
     */
    @PostMapping("/announcement/add/individual/{studentId}")
    public ResponseEntity<?> createIndividualAnnouncement(@PathVariable String studentId,
                                                          @RequestBody AnnouncementRequest announcementRequest,
                                                          HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }

        try {
            Announcement announcement = lecturerService.createIndividualAnnouncement(lecturerId, studentId, announcementRequest);
            return ResponseEntity.ok(announcement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    /**
     * Add a batchwise Announcement
     * Endpoint: POST /api/lecturer/announcement/add/batchwise/{batchId}
     */
    @PostMapping("/announcement/add/batchwise/{batchId}")
    public ResponseEntity<?> createBatchwiseAnnouncement(@PathVariable String batchId,
                                                         @RequestBody AnnouncementRequest announcementRequest,
                                                         HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }

        try {
            List<Announcement> announcements = lecturerService.createBatchwiseAnnouncement(lecturerId, batchId, announcementRequest);
            return ResponseEntity.ok(announcements);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    /**
     * Add an individual Workshop
     * Endpoint: POST /api/lecturer/workshop/add/individual/{studentId}
     */
    @PostMapping("/workshop/add/individual/{studentId}")
    public ResponseEntity<?> createIndividualWorkshop(@PathVariable String studentId,
                                                      @RequestBody WorkshopRequest workshopRequest,
                                                      HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }

        try {
            Workshop workshop = lecturerService.createIndividualWorkshop(lecturerId, studentId, workshopRequest);
            return ResponseEntity.ok(workshop);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    /**
     * Add a batchwise Workshop
     * Endpoint: POST /api/lecturer/workshop/add/batchwise/{batchId}
     */
    @PostMapping("/workshop/add/batchwise/{batchId}")
    public ResponseEntity<?> createBatchwiseWorkshop(@PathVariable String batchId,
                                                     @RequestBody WorkshopRequest workshopRequest,
                                                     HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }

        try {
            List<Workshop> workshops = lecturerService.createBatchwiseWorkshop(lecturerId, batchId, workshopRequest);
            return ResponseEntity.ok(workshops);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    /**
     * Add an individual Interview
     * Endpoint: POST /api/lecturer/interview/add/individual/{studentId}
     */
    @PostMapping("/interview/add/individual/{studentId}")
    public ResponseEntity<?> createIndividualInterview(@PathVariable String studentId,
                                                       @RequestBody InterviewRequest interviewRequest,
                                                       HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }

        try {
            Interview interview = lecturerService.createIndividualInterview(lecturerId, studentId, interviewRequest);
            return ResponseEntity.ok(interview);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    /**
     * Add a batchwise Interview
     * Endpoint: POST /api/lecturer/interview/add/batchwise/{batchId}
     */
    @PostMapping("/interview/add/batchwise/{batchId}")
    public ResponseEntity<?> createBatchwiseInterview(@PathVariable String batchId,
                                                      @RequestBody InterviewRequest interviewRequest,
                                                      HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }

        try {
            List<Interview> interviews = lecturerService.createBatchwiseInterview(lecturerId, batchId, interviewRequest);
            return ResponseEntity.ok(interviews);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // ========== ANNOUNCEMENT ENDPOINTS ==========
    @GetMapping("/announcement/view")
    public ResponseEntity<?> viewAnnouncements(HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }
        try {
            List<AnnouncementResponse> announcements = lecturerService.viewAnnouncementsByLecturer(lecturerId);
            return ResponseEntity.ok(announcements);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    /**
     * GET /api/lecturer/all
     * Fetch all lecturers.
     */
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllLecturers() {
        try {
            List<Lecturer> lecturers = lecturerService.getAllLecturers();
            return ResponseEntity.ok(lecturers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching lecturers: " + e.getMessage());
        }
    }


    @PutMapping("/announcement/update/{id}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable String id, @RequestBody AnnouncementRequest announcementRequest, HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }
        try {
            Announcement updatedAnnouncement = lecturerService.updateAnnouncement(lecturerId, id, announcementRequest);
            return ResponseEntity.ok(updatedAnnouncement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAnnouncement/{announcementId}")
    public ResponseEntity<String> deleteAnnouncement(@PathVariable String announcementId, HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }
        lecturerService.deleteAnnouncementById(lecturerId,announcementId);
        return ResponseEntity.ok("Announcement and associated event deleted successfully.");
    }

    // ========== WORKSHOP ENDPOINTS ==========
    @GetMapping("/workshop/view")
    public ResponseEntity<?> viewWorkshops(HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }
        try {
            List<WorkshopResponse> workshops = lecturerService.viewWorkshopsByLecturer(lecturerId);
            return ResponseEntity.ok(workshops);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/workshop/update/{id}")
    public ResponseEntity<?> updateWorkshop(@PathVariable String id, @RequestBody WorkshopRequest workshopRequest, HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }
        try {
            Workshop updatedWorkshop = lecturerService.updateWorkshop(lecturerId, id, workshopRequest);
            return ResponseEntity.ok(updatedWorkshop);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteWorkshop/{workshopId}")
    public ResponseEntity<String> deleteWorkshop(@PathVariable String workshopId, HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }
        lecturerService.deleteWorkshopById(lecturerId,workshopId);
        return ResponseEntity.ok("Workshop and associated event deleted successfully.");
    }

    // ========== INTERVIEW ENDPOINTS ==========
    @GetMapping("/interview/view")
    public ResponseEntity<?> viewInterviews(HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }
        try {
            List<InterviewResponse> interviews = lecturerService.viewInterviewsByLecturer(lecturerId);
            return ResponseEntity.ok(interviews);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/interview/update/{id}")
    public ResponseEntity<?> updateInterview(@PathVariable String id, @RequestBody InterviewRequest interviewRequest, HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }
        try {
            Interview updatedInterview = lecturerService.updateInterview(lecturerId, id, interviewRequest);
            return ResponseEntity.ok(updatedInterview);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteInterview/{interviewId}")
    public ResponseEntity<String> deleteInterview(@PathVariable String interviewId, HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }
        lecturerService.deleteInterviewById(lecturerId,interviewId);
        return ResponseEntity.ok("Interview and associated event deleted successfully.");
    }

    @GetMapping("/view-messages")
    public ResponseEntity<?> viewMessagesForLecturer(HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");

        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer is not logged in.");
        }

        try {
            List<MessageResponse> messages = lecturerService.getMessagesForLecturer(lecturerId);
            return ResponseEntity.ok(messages);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping("/download-document/{documentId}")
    public ResponseEntity<?> downloadDocument(
            @PathVariable String documentId,
            HttpSession session) {
        try {
            // Get lecturerId from session
            String lecturerId = (String) session.getAttribute("lecturerId");
            if (lecturerId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
            }

            // Fetch the resource
            Resource file = (Resource) lecturerService.downloadDocument(documentId, lecturerId);

            // Extract file name
            String fileName = Paths.get(((org.springframework.core.io.Resource) file).getURI()).getFileName().toString();

            // Determine content type
            String contentType = "application/octet-stream"; // Default binary type
            try {
                contentType = Files.probeContentType(Paths.get(((org.springframework.core.io.Resource) file).getURI()));
            } catch (IOException e) {
                // Log the error and fallback to default content type
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(file);
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * View portfolios of a specific student
     * Endpoint: GET /api/lecturer/portfolio/view/{studentId}
     */
    @GetMapping("/portfolio/view/{studentId}")
    public ResponseEntity<?> viewPortfoliosByStudentId(@PathVariable String studentId, HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId"); // Ensure lecturer is logged in
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer not logged in.");
        }

        try {
            // Fetch portfolios for the given student ID
            List<Portfolio> portfolios = portfolioService.getPortfoliosByStudentId(studentId);
            return ResponseEntity.ok(portfolios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }



}
