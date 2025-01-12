package com.example.acccreation.dto;

import java.sql.Time;
import java.util.Date;

public class AnnouncementResponse {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    private String eventId;

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    private String aId;
    private String name;
    private String type;
    private String content;
    private Date date;
    private Time time;
    private String status;
    private String studentId;
    private String batchId;

    public AnnouncementResponse(String eventId,String aId,String name, String type, String content, Date date, Time time, String status, String studentId, String batchId) {
        this.eventId = eventId;
        this.aId = aId;
        this.name = name;
        this.type = type;
        this.content = content;
        this.date = date;
        this.time = time;
        this.status = status;
        this.studentId = studentId;
        this.batchId = batchId;
    }

    // Getters and Setters...
}
