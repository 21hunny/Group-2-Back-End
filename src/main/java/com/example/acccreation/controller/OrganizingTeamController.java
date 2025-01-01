package com.example.acccreation.controller;

import com.example.acccreation.dto.MessageRequest;
import com.example.acccreation.dto.ProgressUpdateRequest;
import com.example.acccreation.entity.DocumentUpload;
import com.example.acccreation.entity.Message;
import com.example.acccreation.entity.ProgressUpdate;
import com.example.acccreation.service.OrganizingTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/organizing-team")
public class OrganizingTeamController {

    @Autowired
    private OrganizingTeamService organizingTeamService;

    @PostMapping("/contact-event-head")
    public ResponseEntity<?> contactEventHead(
            @RequestBody MessageRequest messageRequest,
            HttpSession session) {
        String senderId = (String) session.getAttribute("userSId");
        String receiverId = organizingTeamService.getLecturerIdForTeam(senderId);
        if (receiverId == null) {
            return ResponseEntity.badRequest().body("No lecturer assigned to the team.");
        }

        try {
            Message message = organizingTeamService.sendMessage(
                    senderId, receiverId, messageRequest.getSubject(), messageRequest.getContent());
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/send-progress-update")
    public ResponseEntity<?> sendProgressUpdate(
            @RequestBody ProgressUpdateRequest progressUpdateRequest,
            HttpSession session) {
        String teamId = organizingTeamService.getTeamIdForStudent((String) session.getAttribute("userSId"));
        String batchId = (String) session.getAttribute("batchId");

        try {
            ProgressUpdate update = organizingTeamService.sendProgressUpdate(
                    batchId, teamId, progressUpdateRequest.getContent());
            return ResponseEntity.ok(update);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/upload-document")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            HttpSession session) {
        String teamId = organizingTeamService.getTeamIdForStudent((String) session.getAttribute("userSId"));
        String lecturerId = organizingTeamService.getLecturerIdForTeam(teamId);

        try {
            DocumentUpload upload = organizingTeamService.uploadDocument(teamId, lecturerId, file);
            return ResponseEntity.ok(upload);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete Message
    @DeleteMapping("/delete-message/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable String messageId) {
        try {
            organizingTeamService.deleteMessage(messageId);
            return ResponseEntity.ok("Message deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete Progress Update
    @DeleteMapping("/delete-progress/{progressId}")
    public ResponseEntity<?> deleteProgressUpdate(@PathVariable String progressId) {
        try {
            organizingTeamService.deleteProgressUpdate(progressId);
            return ResponseEntity.ok("Progress update deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete Document Upload
    @DeleteMapping("/delete-document/{documentId}")
    public ResponseEntity<?> deleteDocumentUpload(@PathVariable String documentId) {
        try {
            organizingTeamService.deleteDocumentUpload(documentId);
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
