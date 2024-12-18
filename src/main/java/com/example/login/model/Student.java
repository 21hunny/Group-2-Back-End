package com.example.login.model;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Entity
public class Student {
    @jakarta.persistence.Id
    @Id
    private String id;  // SID

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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    private String password;
    private Date regDate;
    private String year;
    private String contact;
    private String email;
    private String name;
    private String photo;
    private String role;

    // Foreign Key to Admin (a_id) - assuming you have an Admin table
    @ManyToOne
    @JoinColumn(name = "a_id", referencedColumnName = "id")
    private Admin admin;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    // Getters and Setters
}
