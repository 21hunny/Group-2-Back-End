package com.example.acccreation.service;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.entity.Batch;
import com.example.acccreation.exception.CustomException;
import com.example.acccreation.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BatchRepository batchRepository;

    public Batch createBatch(Batch batch, Admin admin) {
        batch.setAdmin(admin);
        return batchRepository.save(batch);
    }

    public Batch getBatchById(String batchId) {
        return batchRepository.findById(batchId).orElse(null);
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Batch updateBatch(String batchId, Batch batchRequest) {
        Batch existingBatch = batchRepository.findById(batchId)
                .orElseThrow(() -> new CustomException("Batch not found with ID: " + batchId));
        existingBatch.setName(batchRequest.getName());
        existingBatch.setStartDate(batchRequest.getStartDate());
        existingBatch.setDepartment(batchRequest.getDepartment());
        existingBatch.setCourse(batchRequest.getCourse());
        return batchRepository.save(existingBatch);
    }

    public void deleteBatch(String batchId) {
        // Step 1: Retrieve the batch to ensure it exists
        Batch existingBatch = batchRepository.findById(batchId)
                .orElseThrow(() -> new CustomException("Batch not found with ID: " + batchId));

        // Step 2: Construct the dynamic table name (e.g., "batch_<batchId>")
        String tableName = "batch_" + batchId;

        // Step 3: Drop the associated table if it exists
        String dropTableSQL = "DROP TABLE IF EXISTS " + tableName;
        jdbcTemplate.execute(dropTableSQL);

        // Step 4: Delete the batch from the repository
        batchRepository.delete(existingBatch);
    }
}
