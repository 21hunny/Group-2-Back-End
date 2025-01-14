package com.example.acccreation.service;

import com.example.acccreation.dto.PortfolioRequest;
import com.example.acccreation.entity.Portfolio;
import com.example.acccreation.repository.PortfolioRepository;
import com.example.acccreation.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;



    public Portfolio addPortfolio(PortfolioRequest request, String studentId) {
        String maxId = portfolioRepository.findMaxId();
        String newId = CustomIdGenerator.getNextPortfolioId(maxId);

        Portfolio portfolio = new Portfolio();
        portfolio.setId(newId);
        portfolio.setName(request.getName());
        portfolio.setAbout(request.getAbout());
        portfolio.setProjects(request.getProjects());
        portfolio.setPhoto(request.getPhoto());
        portfolio.setBio(request.getBio());
        portfolio.setStudentId(studentId);

        return portfolioRepository.save(portfolio);
    }

    public Portfolio getPortfolio(String id) {
        return portfolioRepository.findById(id);
    }

    public List<Portfolio> getPortfoliosByStudentId(String studentId) {
        return portfolioRepository.findByStudentId(studentId);
    }

    public Portfolio updatePortfolio(String portfolioId, String studentId, PortfolioRequest portfolioRequest) {
        // Fetch the existing portfolio
        Portfolio existingPortfolio = portfolioRepository.findById(portfolioId);
        if (existingPortfolio == null) {
            throw new RuntimeException("Portfolio not found with ID: " + portfolioId);
        }

        // Ensure the portfolio belongs to the logged-in student
        if (!existingPortfolio.getStudentId().equals(studentId)) {
            throw new RuntimeException("Unauthorized to update this portfolio.");
        }

        // Update only fields provided in the request
        existingPortfolio.setName(portfolioRequest.getName());
        existingPortfolio.setAbout(portfolioRequest.getAbout());
        existingPortfolio.setProjects(portfolioRequest.getProjects());
        existingPortfolio.setPhoto(portfolioRequest.getPhoto());
        existingPortfolio.setBio(portfolioRequest.getBio());

        // Save and return the updated portfolio
        return portfolioRepository.update(existingPortfolio);
    }



    public void deletePortfolio(String id) {
        portfolioRepository.delete(id);
    }

//    private String findMaxPortfolioId() {
//        String sql = "SELECT MAX(id) FROM portfolio";
//        return jdbcTemplate.queryForObject(sql, String.class);
//    }
}
