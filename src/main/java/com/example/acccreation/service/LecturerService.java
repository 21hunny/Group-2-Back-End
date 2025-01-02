package com.example.acccreation.service;

import com.example.acccreation.repository.*;
import com.example.acccreation.entity.*;
import com.example.acccreation.dto.*;
import com.example.acccreation.util.CustomIdGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private HttpSession session;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a new Lecturer with an auto-generated ID.
     */
    public Lecturer createLecturer(Lecturer lecturerRequest, Admin admin) {
        String maxId = findMaxLecturerId();
        String newId = CustomIdGenerator.getNextLecturerId(maxId);
        lecturerRequest.setId(newId);
        lecturerRequest.setPassword(passwordEncoder.encode(lecturerRequest.getPassword())); // Hash the password
        lecturerRequest.setAdmin(admin);
        return lecturerRepository.save(lecturerRequest);
    }

    /**
     * Update Lecturer Password.
     */
    public Lecturer updateLecturerPassword(String lecturerId, PasswordUpdateRequest passwordUpdateRequest) {
        Lecturer existingLecturer = getLecturerById(lecturerId);
        if (!passwordEncoder.matches(passwordUpdateRequest.getCurrentPassword(), existingLecturer.getPassword())) {
            throw new RuntimeException("Current password is incorrect.");
        }
        existingLecturer.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword())); // Hash the new password
        return lecturerRepository.save(existingLecturer);
    }


    /**
     * Updates an existing Lecturer.
     */
    public Lecturer updateLecturer(String lecturerId, Lecturer lecturerRequest) {
        Lecturer existingLecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found with ID: " + lecturerId));
        existingLecturer.setName(lecturerRequest.getName());
        existingLecturer.setPassword(passwordEncoder.encode(lecturerRequest.getPassword()));
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

    /**
     * Get Lecturer by ID.
     */
    public Lecturer getLecturerById(String lecturerId) {
        return lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found with ID: " + lecturerId));
    }

    /**
     * Update Lecturer Profile.
     */
    public Lecturer updateLecturerProfile(String lecturerId, Lecturer lecturerRequest) {
        Lecturer existingLecturer = getLecturerById(lecturerId);
        existingLecturer.setName(lecturerRequest.getName());
        existingLecturer.setEmail(lecturerRequest.getEmail());
        existingLecturer.setDepartment(lecturerRequest.getDepartment());
        existingLecturer.setContact(lecturerRequest.getContact());
        existingLecturer.setCourseAssign(lecturerRequest.getCourseAssign());
        return lecturerRepository.save(existingLecturer);
    }



    /**
     * Get all batch IDs.
     */
    public List<String> getAllBatchIds() {
        String sql = "SELECT id FROM batch";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    /**
     * Get all students by batch.
     */
    public List<String> getStudentsByBatch(String batchId) {
        String tableName = "batch_" + batchId;
        String sql = "SELECT id FROM " + tableName;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    // ========================= EVENT METHODS ==============================

    // ========== ANNOUNCEMENT METHODS ==========
    public Announcement createIndividualAnnouncement(String lecturerId, String studentId, AnnouncementRequest announcementRequest) {
        validateStudent(studentId);
        String eventId = generateAndSaveEvent("Announcement", lecturerId, studentId, null);
        return saveAnnouncement(eventId, announcementRequest);
    }

    public List<Announcement> createBatchwiseAnnouncement(String lecturerId, String batchId, AnnouncementRequest announcementRequest) {
        String eventId = generateAndSaveEvent("Announcement", lecturerId, null, batchId);
        Announcement announcement = saveAnnouncement(eventId, announcementRequest);
        return List.of(announcement);
    }


    // ========== WORKSHOP METHODS ==========
    public Workshop createIndividualWorkshop(String lecturerId, String studentId, WorkshopRequest workshopRequest) {
        validateStudent(studentId);
        String eventId = generateAndSaveEvent("Workshop", lecturerId, studentId, null);
        return saveWorkshop(eventId, workshopRequest);
    }

    public List<Workshop> createBatchwiseWorkshop(String lecturerId, String batchId, WorkshopRequest workshopRequest) {
        String eventId = generateAndSaveEvent("Workshop", lecturerId, null, batchId);
        Workshop workshop = saveWorkshop(eventId, workshopRequest);
        return List.of(workshop);
    }



    // ========== INTERVIEW METHODS ==========
    public Interview createIndividualInterview(String lecturerId, String studentId, InterviewRequest interviewRequest) {
        validateStudent(studentId);
        String eventId = generateAndSaveEvent("Interview", lecturerId, studentId, null);
        return saveInterview(eventId, interviewRequest);
    }

    public List<Interview> createBatchwiseInterview(String lecturerId, String batchId, InterviewRequest interviewRequest) {
        String eventId = generateAndSaveEvent("Interview", lecturerId, null, batchId);
        Interview interview = saveInterview(eventId, interviewRequest);
        return List.of(interview);
    }



    // ========================= HELPER METHODS ==============================
    private String generateAndSaveEvent(String eventType, String lecturerId, String studentId, String batchId) {
        String maxEventId = getMaxId("event", "id");
        String eventId = CustomIdGenerator.getNextEventId(maxEventId);

        Event event = new Event();
        event.setId(eventId);
        event.setName(eventType);
        event.setDate(new Date());
        event.setTime(new Time(System.currentTimeMillis()));
        event.setStatus("Assigned");
        event.setLId(lecturerId);
        event.setSId(studentId);
        event.setBId(batchId);

        eventRepository.save(event);
        return eventId;
    }

    private Announcement saveAnnouncement(String eventId, AnnouncementRequest request) {
        String maxAnnouncementId = getMaxId("announcement", "a_id");
        Announcement announcement = new Announcement();
        announcement.setaId(CustomIdGenerator.getNextAnnouncementId(maxAnnouncementId));
        announcement.setType("General");
        announcement.setContent(request.getContent());
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        announcement.setEvent(event); // Associate the event with the announcement

        return announcementRepository.save(announcement);
    }

    private Workshop saveWorkshop(String eventId, WorkshopRequest request) {
        String maxWorkshopId = getMaxId("workshop", "w_id");
        Workshop workshop = new Workshop();
        workshop.setwId(CustomIdGenerator.getNextWorkshopId(maxWorkshopId));
        workshop.setType(request.getType());
        workshop.setContact(request.getContact());
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        workshop.setEvent(event); // Associate the event with the announcement
        return workshopRepository.save(workshop);
    }

    private Interview saveInterview(String eventId, InterviewRequest request) {
        String maxInterviewId = getMaxId("interview", "i_id");
        Interview interview = new Interview();
        interview.setiId(CustomIdGenerator.getNextInterviewId(maxInterviewId));
        interview.setCompanyName(request.getCompanyName());
        interview.setPosition(request.getPosition());
        interview.setMode(request.getMode());
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        interview.setEvent(event); // Associate the event with the announcement
        return interviewRepository.save(interview);
    }

    private void validateStudent(String studentId) {
        String batchId = extractBatchId(studentId);
        String tableName = "batch_" + batchId;
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, studentId);
        if (count == null || count == 0) {
            throw new RuntimeException("Student not found with ID: " + studentId);
        }
    }

    private String extractBatchId(String studentId) {
        return studentId.split("-")[0];
    }

    private String getMaxId(String tableName, String columnName) {
        String sql = "SELECT MAX(" + columnName + ") FROM " + tableName;
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public List<AnnouncementResponse> viewAnnouncementsByLecturer(String lecturerId) {
        return eventRepository.findByLIdAndName(lecturerId, "Announcement").stream()
                .map(event -> {
                    Announcement announcement = announcementRepository.findByEId(event.getId());
                    return new AnnouncementResponse(
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

    public List<WorkshopResponse> viewWorkshopsByLecturer(String lecturerId) {
        return eventRepository.findByLIdAndName(lecturerId, "Workshop").stream()
                .map(event -> {
                    Workshop workshop = workshopRepository.findByEId(event.getId());
                    return new WorkshopResponse(
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

    public List<InterviewResponse> viewInterviewsByLecturer(String lecturerId) {
        return eventRepository.findByLIdAndName(lecturerId, "Interview").stream()
                .map(event -> {
                    Interview interview = interviewRepository.findByEId(event.getId());
                    return new InterviewResponse(
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


    public Announcement updateAnnouncement(String lecturerId, String eventId, AnnouncementRequest updatedRequest) {
        validateLecturerLogin(lecturerId); // Check if the lecturer is logged in
        Event event = validateEventOwnership(lecturerId, eventId, "Announcement"); // Validate ownership
        Announcement announcement = announcementRepository.findByEId(eventId);
        if (announcement == null) {
            throw new RuntimeException("Announcement not found for event ID: " + eventId);
        }
        announcement.setContent(updatedRequest.getContent());
        return announcementRepository.save(announcement);
    }

    public Workshop updateWorkshop(String lecturerId, String eventId, WorkshopRequest updatedRequest) {
        validateLecturerLogin(lecturerId); // Check if the lecturer is logged in
        Event event = validateEventOwnership(lecturerId, eventId, "Workshop"); // Validate ownership
        Workshop workshop = workshopRepository.findByEId(eventId);
        if (workshop == null) {
            throw new RuntimeException("Workshop not found for event ID: " + eventId);
        }
        workshop.setType(updatedRequest.getType());
        workshop.setContact(updatedRequest.getContact());
        return workshopRepository.save(workshop);
    }

    public Interview updateInterview(String lecturerId, String eventId, InterviewRequest updatedRequest) {
        validateLecturerLogin(lecturerId); // Check if the lecturer is logged in
        Event event = validateEventOwnership(lecturerId, eventId, "Interview"); // Validate ownership
        Interview interview = interviewRepository.findByEId(eventId);
        if (interview == null) {
            throw new RuntimeException("Interview not found for event ID: " + eventId);
        }
        interview.setCompanyName(updatedRequest.getCompanyName());
        interview.setPosition(updatedRequest.getPosition());
        interview.setMode(updatedRequest.getMode());
        return interviewRepository.save(interview);
    }



    public void deleteAnnouncementById(String lecturerId, String announcementId) {
        validateLecturerLogin(lecturerId); // Check if the lecturer is logged in
        String eventId = announcementRepository.findEventIdByAnnouncementId(announcementId);
        if (eventId == null) {
            throw new RuntimeException("No event found for the provided announcement ID.");
        }
        validateEventOwnership(lecturerId, eventId, "Announcement"); // Validate ownership
        announcementRepository.deleteById(announcementId); // Delete the announcement
        eventRepository.deleteById(eventId); // Delete the event
    }

    public void deleteWorkshopById(String lecturerId, String workshopId) {
        validateLecturerLogin(lecturerId); // Check if the lecturer is logged in
        String eventId = workshopRepository.findEventIdByWorkshopId(workshopId);
        if (eventId == null) {
            throw new RuntimeException("No event found for the provided workshop ID.");
        }
        validateEventOwnership(lecturerId, eventId, "Workshop"); // Validate ownership
        workshopRepository.deleteById(workshopId); // Delete the workshop
        eventRepository.deleteById(eventId); // Delete the event
    }

    public void deleteInterviewById(String lecturerId, String interviewId) {
        validateLecturerLogin(lecturerId); // Check if the lecturer is logged in
        String eventId = interviewRepository.findEventIdByInterviewId(interviewId);
        if (eventId == null) {
            throw new RuntimeException("No event found for the provided interview ID.");
        }
        validateEventOwnership(lecturerId, eventId, "Interview"); // Validate ownership
        interviewRepository.deleteById(interviewId); // Delete the interview
        eventRepository.deleteById(eventId); // Delete the event
    }
    private FeedbackResponse mapFeedbackToResponse(Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        response.setfId(feedback.getFId());
        response.setContent(feedback.getContent());
        response.setPoints(feedback.getPoints());
        response.setDate(feedback.getDate());
        response.setBatchId(feedback.getBatchId());
        response.setStudentId(feedback.getStudentId());
        response.setLecturerId(feedback.getLecturer().getId());
        return response;
    }

    public FeedbackResponse addIndividualFeedback(String lecturerId, String studentId, String content, int points) {
        String maxFeedbackId = getMaxId("feedback", "f_id");
        String feedbackId = CustomIdGenerator.getNextFeedbackId(maxFeedbackId);

        Feedback feedback = new Feedback();
        feedback.setFId(feedbackId);
        feedback.setContent(content);
        feedback.setPoints(points);
        feedback.setDate(new Date());
        feedback.setStudentId(studentId);
        feedback.setBatchId(extractBatchId(studentId));
        feedback.setLecturer(getLecturerById(lecturerId));

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return mapFeedbackToResponse(savedFeedback);
    }

    public FeedbackResponse addBatchwiseFeedback(String lecturerId, String batchId, String content, int points) {
        String maxFeedbackId = getMaxId("feedback", "f_id");
        String feedbackId = CustomIdGenerator.getNextFeedbackId(maxFeedbackId);

        Feedback feedback = new Feedback();
        feedback.setFId(feedbackId);
        feedback.setContent(content);
        feedback.setPoints(points);
        feedback.setDate(new Date()); // Current system date
        feedback.setBatchId(batchId); // Set batch ID
        feedback.setStudentId(null);  // Set student ID as null for batchwise feedback
        feedback.setLecturer(getLecturerById(lecturerId)); // Fetch and set the lecturer

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return mapFeedbackToResponse(savedFeedback);
    }

    public List<FeedbackResponse> viewFeedbackByBatch(String batchId) {
        List<Feedback> feedbacks = feedbackRepository.findByBatchId(batchId);
        return feedbacks.stream().map(this::mapFeedbackToResponse).toList();
    }

    public List<FeedbackResponse> viewFeedbackByStudent(String studentId) {
        List<Feedback> feedbacks = feedbackRepository.findByStudentId(studentId);
        return feedbacks.stream().map(this::mapFeedbackToResponse).toList();
    }

    public FeedbackResponse updateFeedback(String lecturerId, String feedbackId, String content, int points) {
        validateLecturerLogin(lecturerId);

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + feedbackId));

        feedback.setContent(content);
        feedback.setPoints(points);

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return mapFeedbackToResponse(updatedFeedback);
    }



    /**
     * Delete feedback.
     */
    public void deleteFeedback(String lecturerId, String feedbackId) {
        validateLecturerLogin(lecturerId);

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + feedbackId));

        feedbackRepository.delete(feedback);
    }


    private void validateLecturerLogin(String lecturerId) {
        String loggedInLecturerId = (String) session.getAttribute("userLId");
        if (loggedInLecturerId == null ) {
            throw new RuntimeException("Lecturer is not logged in or session is invalid.");
        }
    }



    private Event validateEventOwnership(String lecturerId, String eventId, String eventType) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        if (!event.getLId().equals(lecturerId) || !event.getName().equals(eventType)) {
            throw new RuntimeException("Unauthorized to access or modify this event.");
        }
        return event;
    }

    public List<MessageResponse> getMessagesForLecturer(String lecturerId) {
        List<Message> messages = messageRepository.findMessagesByReceiverId(lecturerId);

        if (messages == null || messages.isEmpty()) {
            throw new RuntimeException("No messages to show.");
        }

        // Map to DTO
        return messages.stream()
                .map(message -> new MessageResponse(message.getSubject(), message.getContent(), message.getDate()))
                .collect(Collectors.toList());
    }

    public List<Lecturer> getAllLecturers() {
        return lecturerRepository.findAll();
    }


}
