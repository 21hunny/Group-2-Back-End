package com.example.acccreation.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "document_uploads")
public class DocumentUpload {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Id
    private String id;
    private String teamId;
    private String lecturerId;
    private String filePath;
}
