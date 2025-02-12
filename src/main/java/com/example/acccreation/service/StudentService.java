package com.example.acccreation.service;

import com.example.acccreation.dto.AnnouncementResponse;
import com.example.acccreation.dto.InterviewResponse;
import com.example.acccreation.dto.WorkshopResponse;
import com.example.acccreation.entity.*;
import com.example.acccreation.repository.*;
import com.example.acccreation.util.CustomIdGenerator;
import com.example.acccreation.dto.StudentProfileUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a new Student.
     */
    public Student createStudent(Student studentRequest, Admin admin, Batch batch) {
        String batchId = batch.getId();
        createDynamicBatchTableIfNotExists(batchId);
        String maxId = findMaxStudentId(batchId);
        String newId = CustomIdGenerator.getNextStudentId(batchId, maxId);

        studentRequest.setId(newId);
        studentRequest.setPassword(passwordEncoder.encode(studentRequest.getPassword())); // Hash the password
        studentRequest.setAdminId(admin.getId());
        studentRequest.setBatchId(batchId);
        studentRequest.setPhoto(studentRequest.getPhoto());
        studentRequest.setRegDate(new Date());
        studentRequest.setRole("batchmate");

        return studentRepository.save(studentRequest);
    }

    /**
     * Update the student's password.
     */
    public void updatePassword(String studentId, String batchId, String currentPassword, String newPassword) {
        Student existingStudent = studentRepository.findById(studentId, batchId);
        if (existingStudent == null) {
            throw new RuntimeException("Student not found.");
        }
        if (!passwordEncoder.matches(currentPassword, existingStudent.getPassword())) {
            throw new RuntimeException("Current password is incorrect.");
        }
        existingStudent.setPassword(passwordEncoder.encode(newPassword)); // Hash the new password
        studentRepository.update(existingStudent, batchId);
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
                passwordEncoder.encode(studentRequest.getPassword()),
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
                + "photo VARCHAR(100), "
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

    /**
     * Updates the student's profile.
     */
    public Student updateProfile(String studentId, String batchId, StudentProfileUpdateRequest profileRequest) {
        Student student = studentRepository.findById(studentId, batchId);
        if (student == null) {
            throw new RuntimeException("Student not found.");
        }

        // Update fields
        student.setContact(profileRequest.getContact());
        student.setEmail(profileRequest.getEmail());
        student.setName(profileRequest.getName());
        student.setPhoto(profileRequest.getPhoto());

        // Save updated student
        return studentRepository.update(student, batchId);
    }



    public Student getStudent(String studentId) {
        // Extract batchId from studentId
        String batchId = extractBatchId(studentId);
        if (batchId == null || batchId.isEmpty()) {
            throw new RuntimeException("Invalid student ID. Batch ID could not be determined.");
        }

        String tableName = "batch_" + batchId;
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
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
                student.setAdminId(rs.getString("a_id"));
                student.setBatchId(rs.getString("b_id"));
                return student;
            }, studentId);
        } catch (Exception e) {
            throw new RuntimeException("Student not found with ID: " + studentId, e);
        }
    }

    public List<Student> getAllStudents(String batchId) {
        String tableName = "batch_" + batchId;
        String sql = "SELECT * FROM " + tableName;

        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
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
                student.setAdminId(rs.getString("a_id"));
                student.setBatchId(rs.getString("b_id"));
                return student;
            });
        } catch (Exception e) {
            throw new RuntimeException("Error fetching students for batch: " + batchId, e);
        }
    }


    public List<AnnouncementResponse> viewAnnouncementsByBatch(String batchId) {
        List<Event> events = eventRepository.findByName("Announcement")
                .stream()
                .filter(event -> batchId.equals(event.getBId()))
                .toList();
        return events.stream().map(event -> {
            Announcement announcement = announcementRepository.findByEId(event.getId());
            return new AnnouncementResponse(
                    event.getId(),
                    announcement.getaId(),
                    event.getName(),
                    announcement.getType(),
                    announcement.getContent(),
                    event.getDate(),
                    event.getTime(),
                    event.getStatus(),
                    event.getSId(),
                    event.getBId()
            );
        }).toList();
    }

    public List<AnnouncementResponse> viewAnnouncementsByStudent(String studentId) {
        System.out.println("Fetching announcements for studentId: " + studentId);

        // Fetch events without filtering for l_id
        List<Event> events = eventRepository.findByName("Announcement")
                .stream()
                .filter(event -> studentId.equals(event.getSId())) // Filter by studentId
                .toList();

        System.out.println("Filtered events for announcements: " + events);

        return events.stream().map(event -> {
            Announcement announcement = announcementRepository.findByEId(event.getId());
            return new AnnouncementResponse(
                    event.getId(),
                    announcement.getaId(),
                    event.getName(),
                    announcement.getType(),
                    announcement.getContent(),
                    event.getDate(),
                    event.getTime(),
                    event.getStatus(),
                    event.getSId(),
                    event.getBId()
            );
        }).toList();
    }



    public List<WorkshopResponse> viewWorkshopsByBatch(String batchId) {
        List<Event> events = eventRepository.findByName("Workshop")
                .stream()
                .filter(event -> batchId.equals(event.getBId()))
                .toList();
        return events.stream().map(event -> {
            Workshop workshop = workshopRepository.findByEId(event.getId());
            return new WorkshopResponse(
                    event.getId(),
                    workshop.getwId(),
                    event.getName(),
                    workshop.getType(),
                    workshop.getContact(),
                    event.getDate(),
                    event.getTime(),
                    event.getStatus(),
                    event.getSId(),
                    event.getBId()
            );
        }).toList();
    }

    public List<WorkshopResponse> viewWorkshopsByStudent(String studentId) {
        System.out.println("Fetching workshops for studentId: " + studentId);

        // Fetch events directly using the new repository query
        List<Event> events = eventRepository.findByName("Workshop");

        System.out.println("Filtered events for workshops: " + events);

        return events.stream().map(event -> {
            Workshop workshop = workshopRepository.findByEId(event.getId());
            return new WorkshopResponse(
                    event.getId(),
                    workshop.getwId(),
                    event.getName(),
                    workshop.getType(),
                    workshop.getContact(),
                    event.getDate(),
                    event.getTime(),
                    event.getStatus(),
                    event.getSId(),
                    event.getBId()
            );
        }).toList();
    }

    public List<InterviewResponse> viewInterviewsByBatch(String batchId) {
        List<Event> events = eventRepository.findByName("Interview")
                .stream()
                .filter(event -> batchId.equals(event.getBId()))
                .toList();
        return events.stream().map(event -> {
            Interview interview = interviewRepository.findByEId(event.getId());
            return new InterviewResponse(
                    event.getId(),
                    interview.getiId(),
                    event.getName(),
                    interview.getCompanyName(),
                    interview.getPosition(),
                    interview.getMode(),
                    event.getDate(),
                    event.getTime(),
                    event.getStatus(),
                    event.getSId(),
                    event.getBId()
            );
        }).toList();
    }

    public List<InterviewResponse> viewInterviewsByStudent(String studentId) {
        System.out.println("Fetching interviews for studentId: " + studentId);

        // Fetch events directly using the new repository query
        List<Event> events = eventRepository.findByName("Interview");

        System.out.println("Filtered events for interviews: " + events);

        return events.stream().map(event -> {
            Interview interview = interviewRepository.findByEId(event.getId());
            return new InterviewResponse(
                    event.getId(),
                    interview.getiId(),
                    event.getName(),
                    interview.getCompanyName(),
                    interview.getPosition(),
                    interview.getMode(),
                    event.getDate(),
                    event.getTime(),
                    event.getStatus(),
                    event.getSId(),
                    event.getBId()
            );
        }).toList();
    }


}
