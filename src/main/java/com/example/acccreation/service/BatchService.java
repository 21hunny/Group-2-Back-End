package com.example.acccreation.service;

import com.example.acccreation.entity.Batch;
import com.example.acccreation.entity.Admin;
import com.example.acccreation.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;

    public Batch createBatch(Batch batch, Admin admin) {
        // The batch ID is manually assigned from request (e.g. "B2024", "gadse232f", etc.)
        batch.setAdmin(admin);
        return batchRepository.save(batch);
    }

    public Batch getBatchById(String batchId) {
        return batchRepository.findById(batchId).orElse(null);
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }
}
