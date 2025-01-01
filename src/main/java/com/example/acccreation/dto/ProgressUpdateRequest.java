package com.example.acccreation.dto;

import lombok.Data;

@Data
public class ProgressUpdateRequest {
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;
}

