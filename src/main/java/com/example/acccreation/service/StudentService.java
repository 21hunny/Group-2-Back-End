package com.example.acccreation.service;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.entity.Batch;
import com.example.acccreation.entity.Student;
import com.example.acccreation.repository.StudentRepository;
import com.example.acccreation.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Student createStudent(Student studentRequest, Admin admin, Batch batch) {
        String batchId = batch.getId();

        createDynamicBatchTableIfNotExists(batchId);

        String maxId = findMaxStudentId(batchId);
        String newId = CustomIdGenerator.getNextStudentId(batchId, maxId);

        studentRequest.setId(newId);
        studentRequest.setAdminId(admin.getId());
        studentRequest.setBatchId(batchId);
        studentRequest.setRegDate(new Date());

        return studentRepository.save(studentRequest);
    }

    public Student updateStudent(String studentId, Student studentRequest) {
        // Extract batchId from studentId
        String batchId = extractBatchId(studentId);

        if (batchId == null || batchId.isEmpty()) {
            throw new RuntimeException("Invalid student ID. Batch ID could not be determined.");
        }

        String tableName = "batch_" + batchId;

        // Check if the student exists
        String sqlCheck = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, studentId);
        if (count == null || count == 0) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }

        // Update the student
        String sqlUpdate = "UPDATE " + tableName + " SET password = ?, reg_date = ?, year = ?, contact = ?, "
                + "email = ?, name = ?, photo = ?, role = ? WHERE id = ?";
        jdbcTemplate.update(sqlUpdate,
                studentRequest.getPassword(),
                studentRequest.getRegDate(),
                studentRequest.getYear(),
                studentRequest.getContact(),
                studentRequest.getEmail(),
                studentRequest.getName(),
                studentRequest.getPhoto(),
                studentRequest.getRole(),
                studentId);

        return studentRequest;
    }

    /**
     * Deletes a student by ID.
     *
     * @param studentId The ID of the student to delete.
     */
    public void deleteStudent(String studentId) {
        // Extract batchId from studentId
        String batchId = extractBatchId(studentId);

        if (batchId == null || batchId.isEmpty()) {
            throw new RuntimeException("Invalid student ID. Batch ID could not be determined.");
        }

        String tableName = "batch_" + batchId;

        // Check if the student exists
        String sqlCheck = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, studentId);
        if (count == null || count == 0) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }

        // Delete the student
        String sqlDelete = "DELETE FROM " + tableName + " WHERE id = ?";
        jdbcTemplate.update(sqlDelete, studentId);
    }

    /**
     * Extracts the batch ID from the student ID.
     * e.g., studentId "gadse261f-001" -> batchId "gadse261f".
     */
    private String extractBatchId(String studentId) {
        if (studentId == null || !studentId.contains("-")) {
            return null; // Invalid format
        }
        return studentId.split("-")[0]; // Take the part before the "-"
    }

    private void createDynamicBatchTableIfNotExists(String batchId) {
        String tableName = "batch_" + batchId;
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + "id VARCHAR(50) PRIMARY KEY, "
                + "password VARCHAR(100) NOT NULL, "
                + "reg_date DATE, "
                + "year INT, "
                + "contact VARCHAR(50), "
                + "email VARCHAR(100), "
                + "name VARCHAR(100), "
                + "photo LONGBLOB, "
                + "role VARCHAR(50) DEFAULT 'batchmate', "
                + "a_id VARCHAR(50), "
                + "b_id VARCHAR(50), "
                + "FOREIGN KEY (a_id) REFERENCES admin(id), "
                + "FOREIGN KEY (b_id) REFERENCES batch(id))";
        jdbcTemplate.execute(sql);
    }

    private String findMaxStudentId(String batchId) {
        String tableName = "batch_" + batchId;
        String sql = "SELECT MAX(id) FROM " + tableName;
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
