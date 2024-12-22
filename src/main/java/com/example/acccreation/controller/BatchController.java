package com.example.acccreation.controller;

import com.example.acccreation.entity.Batch;
import com.example.acccreation.entity.Admin;
import com.example.acccreation.service.BatchService;
import com.example.acccreation.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private AdminService adminService;

    /**
     * POST /api/batch/add?adminId=A001
     * Creates a new Batch record, associating it with the provided Admin.
     */
    @PostMapping("/add")
    public ResponseEntity<Batch> createBatch(@RequestParam String adminId,
                                             @RequestBody Batch batch) {
        // 1) Fetch the Admin by ID
        Admin admin = adminService.findById(adminId).orElse(null);
        if (admin == null) {
            return ResponseEntity.badRequest().build(); // admin doesn't exist
        }

        // 2) Create the batch
        Batch savedBatch = batchService.createBatch(batch, admin);
        return ResponseEntity.ok(savedBatch);
    }

    /**
     * GET /api/batch/{batchId}
     * Retrieves a specific batch by ID.
     */
    @GetMapping("/get/{batchId}")
    public ResponseEntity<Batch> getBatch(@PathVariable String batchId) {
        Batch batch = batchService.getBatchById(batchId);
        if (batch == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(batch);
    }
}
