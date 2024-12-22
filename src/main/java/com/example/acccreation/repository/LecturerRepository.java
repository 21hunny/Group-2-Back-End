package com.example.acccreation.repository;

import com.example.acccreation.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepository extends JpaRepository<Lecturer, String> {
    // Additional query methods if needed
}
