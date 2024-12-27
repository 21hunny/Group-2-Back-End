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
    private LecturerRepository lecturerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Creates a new Lecturer with an auto-generated ID.
     */
    public Lecturer createLecturer(Lecturer lecturerRequest, Admin admin) {
        String maxId = findMaxLecturerId(); // e.g., "L010" or null
        String newId = CustomIdGenerator.getNextLecturerId(maxId); // e.g., "L011"
        lecturerRequest.setId(newId);
        lecturerRequest.setAdmin(admin);
        return lecturerRepository.save(lecturerRequest);
    }

    /**
     * Updates an existing Lecturer.
     */
    public Lecturer updateLecturer(String lecturerId, Lecturer lecturerRequest) {
        Lecturer existingLecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found with ID: " + lecturerId));
        existingLecturer.setName(lecturerRequest.getName());
        existingLecturer.setPassword(lecturerRequest.getPassword());
        existingLecturer.setEmail(lecturerRequest.getEmail());
        existingLecturer.setDepartment(lecturerRequest.getDepartment());
        existingLecturer.setContact(lecturerRequest.getContact());
        existingLecturer.setCourseAssign(lecturerRequest.getCourseAssign());
        return lecturerRepository.save(existingLecturer);
    }


    /**
     * Deletes a Lecturer by ID.
     */
    public void deleteLecturer(String lecturerId) {
        Lecturer existingLecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found with ID: " + lecturerId));
        lecturerRepository.delete(existingLecturer);
    }

    /**
     * Finds the max Lecturer ID.
     */
    private String findMaxLecturerId() {
        String sql = "SELECT MAX(id) FROM lecturer WHERE id LIKE 'L%'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
