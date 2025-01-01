package com.example.acccreation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "batch")
public class Batch implements Serializable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Id
    private String id; // e.g., "B2024" or "gadse232f"

    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    private String department;
    private String course;

    @ManyToOne
    @JoinColumn(name = "a_id")
    private Admin admin; // The Admin who created this batch

    // Additional methods if needed can go here
}
