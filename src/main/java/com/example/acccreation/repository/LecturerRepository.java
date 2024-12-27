package com.example.acccreation.repository;

import com.example.acccreation.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LecturerRepository extends JpaRepository<Lecturer, String> {
    Optional<Lecturer> findByIdAndPassword(String id, String password); // For Lecturer Login
}
