package com.example.acccreation.service;

import com.example.acccreation.dto.FeedbackResponse;
import com.example.acccreation.entity.Feedback;
import com.example.acccreation.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<FeedbackResponse> viewFeedbackForStudent(String studentId, String batchId) {
        try {
            // Log start
            System.out.println("Fetching individual feedbacks for student: " + studentId);
            List<Feedback> individualFeedbacks = feedbackRepository.findByStudentId(studentId);

            System.out.println("Fetching batch feedbacks for batch: " + batchId);
            List<Feedback> batchFeedbacks = feedbackRepository.findByBatchId(batchId);

            // Merge feedbacks
            System.out.println("Mapping feedbacks to response...");
            return Stream.concat(individualFeedbacks.stream(), batchFeedbacks.stream())
                    .distinct() // Remove duplicates
                    .map(this::mapFeedbackToResponse)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace(); // Log exception details
            throw new RuntimeException("Error fetching feedbacks: " + e.getMessage());
        }
    }


    private FeedbackResponse mapFeedbackToResponse(Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        response.setContent(feedback.getContent());
        response.setPoints(feedback.getPoints());
        response.setDate(feedback.getDate());
        return response;
    }
}
