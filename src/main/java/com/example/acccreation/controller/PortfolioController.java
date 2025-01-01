package com.example.acccreation.controller;

import com.example.acccreation.dto.PortfolioRequest;
import com.example.acccreation.entity.Portfolio;
import com.example.acccreation.service.PortfolioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;



    @PostMapping("/add")
    public ResponseEntity<?> addPortfolio(@RequestBody PortfolioRequest portfolioRequest, HttpSession session) {
        String studentId = (String) session.getAttribute("userSId"); // Get the logged-in student ID
        if (studentId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }
        try {
            Portfolio newPortfolio = portfolioService.addPortfolio(portfolioRequest, studentId);
            return ResponseEntity.ok(newPortfolio);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/view")
    public ResponseEntity<?> getPortfolios(HttpSession session) {
        String studentId = (String) session.getAttribute("userSId");
        if (studentId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }

        try {
            List<Portfolio> portfolios = portfolioService.getPortfoliosByStudentId(studentId);
            return ResponseEntity.ok(portfolios);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePortfolio(
            @PathVariable String id,
            @RequestBody PortfolioRequest portfolioRequest,
            HttpSession session) {
        String studentId = (String) session.getAttribute("userSId"); // Get the logged-in student ID
        if (studentId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }
        try {
            Portfolio updatedPortfolio = portfolioService.updatePortfolio(id, studentId, portfolioRequest);
            return ResponseEntity.ok(updatedPortfolio);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePortfolio(@PathVariable String id, HttpSession session) {
        String studentId = (String) session.getAttribute("userSId");
        if (studentId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }

        try {
            portfolioService.deletePortfolio(id);
            return ResponseEntity.ok("Portfolio deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
