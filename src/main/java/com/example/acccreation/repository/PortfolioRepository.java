package com.example.acccreation.repository;

import com.example.acccreation.entity.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PortfolioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Portfolio save(Portfolio portfolio) {
        String sql = "INSERT INTO portfolio (id, name, about, projects, photo, bio, s_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                portfolio.getId(),
                portfolio.getName(),
                portfolio.getAbout(),
                portfolio.getProjects(),
                portfolio.getPhoto(),
                portfolio.getBio(),
                portfolio.getStudentId());
        return portfolio;
    }


    public String findMaxId() {
        String sql = "SELECT MAX(id) FROM portfolio";
        return jdbcTemplate.queryForObject(sql, String.class);
    }


    public Portfolio findById(String id) {
        String sql = "SELECT * FROM portfolio WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Portfolio portfolio = new Portfolio();
            portfolio.setId(rs.getString("id"));
            portfolio.setName(rs.getString("name"));
            portfolio.setAbout(rs.getString("about"));
            portfolio.setProjects(rs.getString("projects"));
            portfolio.setPhoto(rs.getString("photo"));
            portfolio.setBio(rs.getString("bio"));
            portfolio.setStudentId(rs.getString("s_id"));
            return portfolio;
        });
    }

    public List<Portfolio> findByStudentId(String studentId) {
        String sql = "SELECT * FROM portfolio WHERE s_id = ?";
        return jdbcTemplate.query(sql, new Object[]{studentId}, (rs, rowNum) -> {
            Portfolio portfolio = new Portfolio();
            portfolio.setId(rs.getString("id"));
            portfolio.setName(rs.getString("name"));
            portfolio.setAbout(rs.getString("about"));
            portfolio.setProjects(rs.getString("projects"));
            portfolio.setPhoto(rs.getString("photo"));
            portfolio.setBio(rs.getString("bio"));
            portfolio.setStudentId(rs.getString("s_id"));
            return portfolio;
        });
    }

    public Portfolio update(Portfolio portfolio) {
        String sql = "UPDATE portfolio SET name = ?, about = ?, projects = ?, photo = ?, bio = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                portfolio.getName(),
                portfolio.getAbout(),
                portfolio.getProjects(),
                portfolio.getPhoto(),
                portfolio.getBio(),
                portfolio.getId()
        );
        return portfolio;
    }

    public void delete(String id) {
        String sql = "DELETE FROM portfolio WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
