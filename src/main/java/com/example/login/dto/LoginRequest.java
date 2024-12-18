package com.example.login.dto;

public class LoginRequest {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String userId; // For Student/Lecturer: ID; For Admin: Username
    private String password;
}
