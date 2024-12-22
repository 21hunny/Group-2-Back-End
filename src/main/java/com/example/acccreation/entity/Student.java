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
    private String batchId; // which batch does the student belong to
}
