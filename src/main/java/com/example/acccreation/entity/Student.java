package com.example.acccreation.entity;

import lombok.*;
import java.util.Date;

/**
 * Represents a student who will be stored in batch_<batchId> tables only.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    // e.g. "B2024-001"
    private String id;

    private String password;
    private Date regDate;
    private int year;
    private String contact;
    private String email;
    private String name;
    private String photo;   // optional
    private String role;    // e.g. "STUDENT"

    // References for the logic
    private String adminId; // which admin created this student

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    private String batchId; // which batch does the student belong to
}