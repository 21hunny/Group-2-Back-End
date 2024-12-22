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

    /**
     * Creates a new student in table 'batch_<batchId>' with an auto-generated ID
     * e.g. 'gadse232f-044'.
     */
    public Student createStudent(Student studentRequest, Admin admin, Batch batch) {
        String batchId = batch.getId();

        // 1) Create batch_<batchId> if not exists
        createDynamicBatchTableIfNotExists(batchId);

        // 2) find max ID => e.g. "gadse232f-043"
        String maxId = findMaxStudentId(batchId);

        // 3) Generate next => "gadse232f-044"
        String newId = CustomIdGenerator.getNextStudentId(batchId, maxId);

        // 4) Populate the student
        studentRequest.setId(newId);
        studentRequest.setAdminId(admin.getId());
        studentRequest.setBatchId(batchId);
        studentRequest.setRegDate(new Date());

        // 5) Save via custom repository logic
        return studentRepository.save(studentRequest);
    }

    private void createDynamicBatchTableIfNotExists(String batchId) {
        // Validate batchId if needed
        String tableName = "batch_" + batchId;
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + "  id VARCHAR(50) PRIMARY KEY, "
                + "  password VARCHAR(100) NOT NULL, "
                + "  reg_date DATE, "
                + "  year INT, "
                + "  contact VARCHAR(50), "
                + "  email VARCHAR(100), "
                + "  name VARCHAR(100), "
                + "  photo LONGBLOB, "
                + "  role VARCHAR(50) DEFAULT 'batchmate', "
                + "  a_id VARCHAR(50), "
                + "  b_id VARCHAR(50), "
                + "  FOREIGN KEY (a_id) REFERENCES admin(id), "
                + "  FOREIGN KEY (b_id) REFERENCES batch(id)"
                + ")";
        jdbcTemplate.execute(sql);
    }

    private String findMaxStudentId(String batchId) {
        String tableName = "batch_" + batchId;
        String sql = "SELECT MAX(id) FROM " + tableName;
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
