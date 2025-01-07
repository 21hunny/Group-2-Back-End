package com.example.acccreation.repository;

import com.example.acccreation.entity.DocumentUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, String> {
    @Query("SELECT MAX(d.id) FROM DocumentUpload d")
    String findMaxId();

    @Query("SELECT d FROM DocumentUpload d WHERE d.id = :documentId AND d.lecturerId = :lecturerId")
    Optional<DocumentUpload> findByIdAndLecturerId(String documentId, String lecturerId);

}
