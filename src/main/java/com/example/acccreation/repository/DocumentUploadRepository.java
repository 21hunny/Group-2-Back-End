package com.example.acccreation.repository;

import com.example.acccreation.entity.DocumentUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, String> {
    @Query("SELECT MAX(d.id) FROM DocumentUpload d")
    String findMaxId();
}
