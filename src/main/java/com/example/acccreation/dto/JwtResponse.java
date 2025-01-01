package com.example.acccreation.dto;

public class JwtResponse {
    private String token;
    private String userId;
    private String role;

    public JwtResponse(String token, String userId, String role) {
        this.token = token;
        this.userId = userId;
        this.role = role;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
