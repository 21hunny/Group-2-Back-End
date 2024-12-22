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
        String tableName = "batch_" + student.getBatchId();
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
                student.getBatchId()
        );
        return student;
    }

    // other custom queries
}
