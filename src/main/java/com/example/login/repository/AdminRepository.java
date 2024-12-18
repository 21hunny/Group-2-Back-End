package com.example.login.repository;

import com.example.login.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUserName(String userName); // Find admin by username

    boolean existsByUserName(String userName); // Check if a user exists by username
}
