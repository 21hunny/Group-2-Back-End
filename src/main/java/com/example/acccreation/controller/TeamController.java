package com.example.acccreation.controller;

import com.example.acccreation.dto.TeamRequest;
import com.example.acccreation.entity.Team;
import com.example.acccreation.service.TeamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/add")
    public ResponseEntity<?> createTeam(@RequestBody TeamRequest teamRequest, HttpSession session) {
        String leaderId = (String) session.getAttribute("userSId");
        String batchId = (String) session.getAttribute("batchId");

        if (leaderId == null || batchId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }

        try {
            Team team = teamService.createTeam(
                    leaderId,
                    teamRequest.getMembers(),
                    teamRequest.getDescription(),
                    batchId,
                    teamRequest.getLecturerId()
            );
            return ResponseEntity.ok(team);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTeam(@PathVariable String id, @RequestBody TeamRequest teamRequest, HttpSession session) {
        String leaderId = (String) session.getAttribute("userSId");

        if (leaderId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }

        try {
            Team updatedTeam = teamService.updateTeam(
                    id,
                    leaderId,
                    teamRequest.getMembers(),
                    teamRequest.getDescription(),
                    teamRequest.getLecturerId()
            );
            return ResponseEntity.ok(updatedTeam);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable String teamId, HttpSession session) {
        String leaderId = (String) session.getAttribute("userSId");

        if (leaderId == null) {
            return ResponseEntity.status(401).body("Student is not logged in.");
        }

        try {
            teamService.deleteTeam(teamId, leaderId);
            return ResponseEntity.ok("Team deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/view/batch")
    public ResponseEntity<?> viewTeamsByBatch(HttpSession session) {
        String batchId = (String) session.getAttribute("batchId");
        List<Team> teams = teamService.viewTeamsByBatch(batchId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/students/inAteam")
    public ResponseEntity<?> studentsInTeam(HttpSession session) {
        String batchId = (String) session.getAttribute("batchId");

        if (batchId == null || batchId.isEmpty()) {
            return ResponseEntity.status(400).body("Batch ID is missing in the session. Please log in again.");
        }

        try {
            List<String> students = teamService.studentsInTeam(batchId);
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error fetching students in a team: " + e.getMessage());
        }
    }

    @GetMapping("/students/notInAteam")
    public ResponseEntity<?> studentsNotInTeam(HttpSession session) {
        String batchId = (String) session.getAttribute("batchId");

        if (batchId == null || batchId.isEmpty()) {
            return ResponseEntity.status(400).body("Batch ID is missing in the session. Please log in again.");
        }

        try {
            List<String> students = teamService.studentsNotInTeam(batchId);
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error fetching students not in a team: " + e.getMessage());
        }
    }

}

