package com.example.acccreation.repository;

import com.example.acccreation.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Student save(Student student) {
        String tableName = getTableName(student.getBatchId());
        String insertSql = "INSERT INTO " + tableName + " ("
                + "id, password, reg_date, year, contact, email, name, photo, role, a_id, b_id"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertSql,
                student.getId(),
                student.getPassword(),
                student.getRegDate(),
                student.getYear(),
                student.getContact(),
                student.getEmail(),
                student.getName(),
                student.getPhoto(),
                student.getRole(),
                student.getAdminId(),
                student.getBatchId());
        return student;
    }

    /**
     * Find a student by ID and batch.
     */
    public Student findById(String studentId, String batchId) {
        String tableName = getTableName(batchId);
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{studentId}, (rs, rowNum) -> {
            Student student = new Student();
            student.setId(rs.getString("id"));
            student.setPassword(rs.getString("password"));
            student.setRegDate(rs.getDate("reg_date"));
            student.setYear(rs.getInt("year"));
            student.setContact(rs.getString("contact"));
            student.setEmail(rs.getString("email"));
            student.setName(rs.getString("name"));
            student.setPhoto(rs.getString("photo"));
            student.setRole(rs.getString("role"));
            student.setBatchId(batchId);
            return student;
        });
    }

    /**
     * Update a student in the batch table.
     */
    public Student update(Student student, String batchId) {
        String tableName = getTableName(batchId);
        String updateSql = "UPDATE " + tableName + " SET "
                + "contact = ?, email = ?, name = ?, photo = ?, password = ? "
                + "WHERE id = ?";
        jdbcTemplate.update(updateSql,
                student.getContact(),
                student.getEmail(),
                student.getName(),
                student.getPhoto(),
                student.getPassword(),
                student.getId());
        return student;
    }

    /**
     * Utility method to get the correct table name for a batch.
     */
    private String getTableName(String batchId) {
        return batchId.startsWith("batch_") ? batchId : "batch_" + batchId;
    }
}
