package com.example.acccreation.controller;

import com.example.acccreation.dto.FeedbackRequest;
import com.example.acccreation.dto.FeedbackResponse;
import com.example.acccreation.service.FeedbackService;
import com.example.acccreation.service.LecturerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/add/individual/{studentId}")
    public ResponseEntity<?> addIndividualFeedback(
            @PathVariable String studentId,
            @RequestBody FeedbackRequest feedbackRequest,
            HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer is not logged in.");
        }
        try {
            FeedbackResponse feedback = lecturerService.addIndividualFeedback(
                    lecturerId, studentId, feedbackRequest.getContent(), feedbackRequest.getPoints());
            return ResponseEntity.ok(feedback);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add/batchwise/{batchId}")
    public ResponseEntity<?> addBatchwiseFeedback(
            @PathVariable String batchId,
            @RequestBody FeedbackRequest feedbackRequest,
            HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer is not logged in.");
        }
        try {
            FeedbackResponse feedback = lecturerService.addBatchwiseFeedback(
                    lecturerId, batchId, feedbackRequest.getContent(), feedbackRequest.getPoints());
            return ResponseEntity.ok(feedback);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateFeedback(
            @PathVariable String id,
            @RequestBody FeedbackRequest feedbackRequest,
            HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer is not logged in.");
        }
        try {
            FeedbackResponse feedback = lecturerService.updateFeedback(
                    lecturerId, id, feedbackRequest.getContent(), feedbackRequest.getPoints());
            return ResponseEntity.ok(feedback);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFeedback(
            @PathVariable String id,
            HttpSession session) {
        String lecturerId = (String) session.getAttribute("userLId");
        if (lecturerId == null) {
            return ResponseEntity.status(401).body("Lecturer is not logged in.");
        }
        try {
            lecturerService.deleteFeedback(lecturerId, id);
            return ResponseEntity.ok("Feedback deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/view/batch/{batchId}")
    public ResponseEntity<?> viewFeedbackByBatch(@PathVariable String batchId) {
        try {
            List<FeedbackResponse> feedbacks = lecturerService.viewFeedbackByBatch(batchId);
            return ResponseEntity.ok(feedbacks);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/view/student/{studentId}")
    public ResponseEntity<?> viewFeedbackByStudent(@PathVariable String studentId) {
        try {
            List<FeedbackResponse> feedbacks = lecturerService.viewFeedbackByStudent(studentId);
            return ResponseEntity.ok(feedbacks);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/view/profile/student")
    public ResponseEntity<?> viewFeedbackForStudent(HttpSession session) {
        String studentId = (String) session.getAttribute("userSId");
        String batchId = (String) session.getAttribute("batchId");

        if (studentId == null || batchId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }

        try {
            System.out.println("Fetching feedbacks for Student ID: " + studentId + ", Batch ID: " + batchId);
            List<FeedbackResponse> feedbacks = feedbackService.viewFeedbackForStudent(studentId, batchId);
            System.out.println("Feedbacks fetched: " + feedbacks);
            return ResponseEntity.ok(feedbacks);
        } catch (RuntimeException e) {
            e.printStackTrace(); // Log the full stack trace
            return ResponseEntity.badRequest().body("An error occurred: " + e.getMessage());
        }
    }

}
