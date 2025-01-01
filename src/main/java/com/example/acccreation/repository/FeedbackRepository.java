package com.example.acccreation.repository;

import com.example.acccreation.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {
    List<Feedback> findByBatchId(String batchId);
    List<Feedback> findByStudentId(String studentId);
    List<Feedback> findByLecturerId(String lecturerId);
}
