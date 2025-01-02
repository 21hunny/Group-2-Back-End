package com.example.acccreation.controller;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.entity.Batch;
import com.example.acccreation.exception.CustomException;
import com.example.acccreation.repository.AdminRepository;
import com.example.acccreation.service.AdminService;
import com.example.acccreation.service.BatchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    private BatchService batchService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    /**
     * POST /api/batch/add
     * Creates a new Batch record, associating it with the logged-in Admin.
     */
    @PostMapping("/add")
    public ResponseEntity<Batch> createBatch(@RequestBody Batch batch, HttpSession session) {
        String adminId = (String) session.getAttribute("userAId");
        if (adminId == null || adminId.isEmpty()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException("Admin not logged in"));
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

    @GetMapping("getAll")
    public ResponseEntity<?> getAllBatches() {
        try {
            List<Batch> batchIds = batchService.getAllBatches();
            return ResponseEntity.ok(batchIds);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching batches: " + e.getMessage());
        }
    }

    /**
     * PUT /api/batch/update/{batchId}
     * Updates an existing batch.
     */
    @PutMapping("/update/{batchId}")
    public ResponseEntity<?> updateBatch(@PathVariable String batchId, @RequestBody Batch batchRequest) {
        try {
            Batch updatedBatch = batchService.updateBatch(batchId, batchRequest);
            return ResponseEntity.ok(updatedBatch); // Use this for successful response
        } catch (CustomException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Use .body() for attaching a message
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error while updating batch: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{batchId}")
    public ResponseEntity<?> deleteBatch(@PathVariable String batchId) {
        try {
            batchService.deleteBatch(batchId);
            return ResponseEntity.ok("Batch deleted successfully"); // Return success message
        } catch (CustomException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Attach the error message
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error while deleting batch: " + e.getMessage());
        }
    }

}
