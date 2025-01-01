package com.example.acccreation.service;

import com.example.acccreation.entity.Team;
import com.example.acccreation.repository.TeamRepository;
import com.example.acccreation.util.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Team createTeam(String leaderId, String members, String description, String batchId, String lecturerId) {
        if (members.split(",").length > 10) {
            throw new RuntimeException("A team can have a maximum of 10 members.");
        }

        if (batchId == null || batchId.isEmpty()) {
            throw new RuntimeException("Batch ID is invalid.");
        }

        String maxId = teamRepository.findMaxId(); // Custom method to find the current max ID
        String teamId = CustomIdGenerator.getNextTeamId(maxId);

        // Create the team
        Team team = new Team();
        team.setId(teamId);
        team.setLeaderId(leaderId);
        team.setMembers(members);
        team.setDescription(description);
        team.setBatchId(batchId); // Ensure batch ID is set
        team.setLecturerId(lecturerId);

        // Save the team
        teamRepository.save(team);

        // Update student roles
        String[] memberIds = members.split(",");

        // Update leader role
        String leaderTable = "batch_" + batchId; // Assuming each batch has a table like "students_batchId"
        String updateLeaderRoleSql = "UPDATE " + leaderTable + " SET role = 'Leader' WHERE id = ?";
        jdbcTemplate.update(updateLeaderRoleSql, leaderId);

        // Update member roles
        String updateMemberRoleSql = "UPDATE " + leaderTable + " SET role = 'Member' WHERE id = ?";
        for (String memberId : memberIds) {
            if (!memberId.equals(leaderId)) {
                jdbcTemplate.update(updateMemberRoleSql, memberId.trim());
            }
        }

        return team;
    }


    public Team updateTeam(String teamId, String leaderId, String members, String description, String lecturerId) {
        // Fetch existing team
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

        // Ensure the leader ID matches
        if (!team.getLeaderId().equals(leaderId)) {
            throw new RuntimeException("Unauthorized to update this team.");
        }

        // Get current members
        String[] currentMembers = team.getMembers().split(",");
        String[] updatedMembers = members.split(",");

        // Dynamically construct the table name for the batch
        String tableName = "batch_" + team.getBatchId();

        // Find removed members
        for (String currentMember : currentMembers) {
            if (!Arrays.asList(updatedMembers).contains(currentMember.trim())) {
                // Update role to batchmate for removed members
                String updateRoleSql = "UPDATE " + tableName + " SET role = 'Batchmate' WHERE id = ?";
                jdbcTemplate.update(updateRoleSql, currentMember.trim());
            }
        }

        // Update fields
        team.setMembers(members);
        team.setDescription(description);
        team.setLecturerId(lecturerId);

        return teamRepository.save(team);
    }


    public void deleteTeam(String teamId, String leaderId) {
        // Fetch existing team
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

        // Ensure the leader ID matches
        if (!team.getLeaderId().equals(leaderId)) {
            throw new RuntimeException("Unauthorized to delete this team.");
        }

        // Get members and leader
        String[] members = team.getMembers().split(",");
        String leader = team.getLeaderId();

        // Dynamically construct the table name for the batch
        String tableName = "batch_" + team.getBatchId();

        // Update roles for all members and the leader
        String updateRoleSql = "UPDATE " + tableName + " SET role = 'Batchmate' WHERE id = ?";
        for (String member : members) {
            jdbcTemplate.update(updateRoleSql, member.trim());
        }
        jdbcTemplate.update(updateRoleSql, leader);

        // Delete the team
        teamRepository.delete(team);
    }



    public List<Team> viewTeamsByBatch(String batchId) {
        return teamRepository.findAllByBatchId(batchId);
    }

    public List<String> studentsInTeam(String batchId) {
        // Dynamically construct the table name for the batch
        String tableName = "batch_" + batchId; // Ensure this matches your database table naming convention

        // Use String.format to safely embed the table name directly into the query
        String sql = String.format("""
        SELECT DISTINCT s.id
        FROM %s s
        WHERE EXISTS (
            SELECT 1
            FROM team t
            WHERE t.b_id = ?
            AND (FIND_IN_SET(s.id, t.members) > 0 OR s.id = t.leader_id)
        )
    """, tableName);

        try {
            // Execute the query
            return jdbcTemplate.queryForList(sql, String.class, batchId);
        } catch (Exception e) {
            throw new RuntimeException("Error executing query: " + e.getMessage());
        }
    }



    public List<String> studentsNotInTeam(String batchId) {
        // Dynamically construct the table name for the batch
        String tableName = "batch_" + batchId; // Ensure this matches your database table naming convention

        // Use String.format to safely embed the table name directly into the query
        String sql = String.format("""
        SELECT DISTINCT s.id
        FROM %s s
        WHERE NOT EXISTS (
            SELECT 1
            FROM team t
            WHERE t.b_id = ?
            AND (FIND_IN_SET(s.id, t.members) > 0 OR s.id = t.leader_id)
        )
    """, tableName);

        try {
            // Execute the query
            return jdbcTemplate.queryForList(sql, String.class, batchId);
        } catch (Exception e) {
            throw new RuntimeException("Error executing query: " + e.getMessage());
        }
    }






}
