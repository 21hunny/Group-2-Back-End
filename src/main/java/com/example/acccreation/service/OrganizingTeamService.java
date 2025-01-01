package com.example.acccreation.service;

import com.example.acccreation.dto.ProgressUpdateResponse;
import com.example.acccreation.entity.DocumentUpload;
import com.example.acccreation.entity.Message;
import com.example.acccreation.entity.ProgressUpdate;
import com.example.acccreation.repository.DocumentUploadRepository;
import com.example.acccreation.repository.MessageRepository;
import com.example.acccreation.repository.ProgressUpdateRepository;
import com.example.acccreation.repository.TeamRepository;
import com.example.acccreation.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrganizingTeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ProgressUpdateRepository progressUpdateRepository;

    @Autowired
    private DocumentUploadRepository documentUploadRepository;

    public Message sendMessage(String senderId, String receiverId, String subject, String content) {
        String maxId = messageRepository.findMaxId();
        String messageId = CustomIdGenerator.getNextMessageId(maxId);

        Message message = new Message();
        message.setId(messageId);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setSubject(subject);
        message.setContent(content);

        return messageRepository.save(message);
    }

    public ProgressUpdate sendProgressUpdate(String batchId, String teamId, String content) {
        String maxId = progressUpdateRepository.findMaxId();
        String updateId = CustomIdGenerator.getNextProgressUpdateId(maxId);

        ProgressUpdate update = new ProgressUpdate();
        update.setId(updateId);
        update.setBatchId(batchId);
        update.setTeamId(teamId);
        update.setContent(content);

        return progressUpdateRepository.save(update);
    }

    public DocumentUpload uploadDocument(String teamId, String lecturerId, MultipartFile file) throws RuntimeException {
        String maxId = documentUploadRepository.findMaxId();
        String documentId = CustomIdGenerator.getNextDocumentUploadId(maxId);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = "E:\\NIBM\\HDSE\\EAD2\\CW\\CW\\uploads/" + fileName;

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the file: " + e.getMessage());
        }

        DocumentUpload document = new DocumentUpload();
        document.setId(documentId);
        document.setTeamId(teamId);
        document.setLecturerId(lecturerId);
        document.setFilePath(filePath);

        return documentUploadRepository.save(document);
    }


    public String getTeamIdForStudent(String studentId) {
        return teamRepository.findTeamIdByStudentId(studentId);
    }

    public String getLecturerIdForTeam(String leaderId) {
        List<String> lecturerIds = teamRepository.findLecturerIdsByLeaderId(leaderId);
        if (lecturerIds.isEmpty()) {
            throw new RuntimeException("No lecturer found for the team leader.");
        }
        if (lecturerIds.size() > 1) {
            throw new RuntimeException("Multiple lecturers found for the team leader. Please check the data.");
        }

        return lecturerIds.get(0); // Return the single lecturer ID
    }

    public List<ProgressUpdateResponse> getProgressUpdatesByBatch(String batchId) {
        List<ProgressUpdate> updates = progressUpdateRepository.findByBatchId(batchId);

        if (updates == null || updates.isEmpty()) {
            throw new RuntimeException("No progress to show.");
        }

        // Map to DTO
        return updates.stream()
                .map(update -> new ProgressUpdateResponse(update.getContent(), update.getDate()))
                .collect(Collectors.toList());
    }

    // Delete Message
    public void deleteMessage(String messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new RuntimeException("Message not found with ID: " + messageId);
        }
        messageRepository.deleteById(messageId);
    }

    // Delete Progress Update
    public void deleteProgressUpdate(String progressUpdateId) {
        if (!progressUpdateRepository.existsById(progressUpdateId)) {
            throw new RuntimeException("Progress update not found with ID: " + progressUpdateId);
        }
        progressUpdateRepository.deleteById(progressUpdateId);
    }

    // Delete Document Upload
    public void deleteDocumentUpload(String documentId) {
        DocumentUpload document = documentUploadRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

        // Delete file from file system
        File file = new File(document.getFilePath());
        if (file.exists()) {
            file.delete();
        }

        // Delete record from database
        documentUploadRepository.deleteById(documentId);
    }

}
