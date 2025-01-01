package com.example.acccreation.dto;

import java.util.Date;

public class ProgressUpdateResponse {
    private String content;
    private Date date;

    // Constructors
    public ProgressUpdateResponse(String content, Date date) {
        this.content = content;
        this.date = date;
    }

    // Getters and Setters
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
}
