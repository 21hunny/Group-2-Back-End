package com.example.acccreation.repository;

import com.example.acccreation.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findById(String UserId);
    Optional<Admin> findByUserName(String userName);
    Optional<Admin> findByEmail(String email);
}