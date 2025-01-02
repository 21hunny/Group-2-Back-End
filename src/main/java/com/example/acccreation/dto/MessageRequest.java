package com.example.acccreation.dto;

import lombok.Data;

@Data
public class MessageRequest {
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private String subject;
    private String content;
}
