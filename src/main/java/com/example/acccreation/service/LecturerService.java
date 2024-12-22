package com.example.acccreation.service;

import com.example.acccreation.entity.Admin;
import com.example.acccreation.entity.Lecturer;
import com.example.acccreation.repository.LecturerRepository;
import com.example.acccreation.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LecturerService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LecturerRepository lecturerRepository;

    /**
     * Creates a new Lecturer with an auto-generated "L###" ID.
     */
    public Lecturer createLecturer(Lecturer lecturerRequest, Admin admin) {
        // 1) find the max ID in 'lecturer' table
        String maxId = findMaxLecturerId(); // "L010" or null

        // 2) generate next ID => "L011"
        String newId = CustomIdGenerator.getNextLecturerId(maxId);
        lecturerRequest.setId(newId);

        // 3) Associate with the Admin object if needed
        lecturerRequest.setAdmin(admin);
        // 4) Save
        return lecturerRepository.save(lecturerRequest);
    }

    /** SELECT MAX(id) FROM lecturer WHERE id LIKE 'L%' */
    private String findMaxLecturerId() {
        String sql = "SELECT MAX(id) FROM lecturer WHERE id LIKE 'L%'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}


