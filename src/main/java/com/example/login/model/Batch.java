package com.example.login.model;

import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Entity
public class Batch {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @jakarta.persistence.Id
    @Id
    private String id; // Assuming `id` is the batch ID (BID)
    private String name;
    private Date startDate;
    private String department;
    private String course;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    // Getters and Setters
}

