package com.example.acccreation.repository;

import com.example.acccreation.entity.ProgressUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgressUpdateRepository extends JpaRepository<ProgressUpdate, String> {
    @Query("SELECT MAX(p.id) FROM ProgressUpdate p")
    String findMaxId();

    @Query("SELECT p FROM ProgressUpdate p WHERE p.batchId = :batchId ORDER BY p.date DESC")
    List<ProgressUpdate> findByBatchId(String batchId);
}
