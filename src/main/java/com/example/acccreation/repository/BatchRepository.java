package com.example.acccreation.repository;

import com.example.acccreation.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, String> {
    // Additional query methods if needed
}
