package com.example.acccreation.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "feedback")
public class Feedback {

    @Id
    @Column(name = "f_id", nullable = false, unique = true, length = 50)
    private String fId;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "batch_id", length = 50) // Dynamically referenced batch ID
    private String batchId;

    @Column(name = "student_id", length = 50) // Dynamically referenced student ID
    private String studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = false) // Foreign Key to Lecturer
    private Lecturer lecturer; // Lecturer entity reference
}
