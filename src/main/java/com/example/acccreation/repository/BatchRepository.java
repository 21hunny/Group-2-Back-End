package com.example.acccreation.repository;

import com.example.acccreation.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, String> {
    List<Batch> findAll();  // Retrieve all batches
}
