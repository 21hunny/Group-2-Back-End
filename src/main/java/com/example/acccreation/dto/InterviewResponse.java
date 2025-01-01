package com.example.acccreation.dto;
import java.sql.Time;
import java.util.Date;
public class InterviewResponse {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    private String name;
    private String companyName;
    private String position;
    private String mode;
    private Date date;
    private Time time;
    private String status;
    private String studentId;
    private String batchId;

    public InterviewResponse(String name, String companyName, String position, String mode, Date date, Time time, String status, String studentId, String batchId) {
        this.name = name;
        this.companyName = companyName;
        this.position = position;
        this.mode = mode;
        this.date = date;
        this.time = time;
        this.status = status;
        this.studentId = studentId;
        this.batchId = batchId;
    }

    // Getters and Setters...
}

