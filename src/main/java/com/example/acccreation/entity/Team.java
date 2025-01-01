package com.example.acccreation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Entity
@Table(name = "team")
public class Team {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @jakarta.persistence.Id
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "leader_id", nullable = false)
    private String leaderId;

    @Column(name = "members", nullable = false)
    private String members; // Comma-separated member IDs

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "b_id", nullable = false) // Make sure this matches the database schema
    private String batchId; // Foreign key to batch

    @Column(name = "l_id", nullable = false)
    private String lecturerId; // Supervising lecturer's ID


}
