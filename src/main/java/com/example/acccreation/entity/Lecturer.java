package com.example.acccreation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lecturer")
public class Lecturer {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 20)
    private String id; // e.g., "L001"

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "contact", length = 15)
    private String contact;

    @Column(name = "course_assign", length = 100)
    private String courseAssign;

    // Many lecturers can be created by one Admin
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "a_id", nullable = false)
    @JsonIgnore
    private Admin admin;  // References the Admin who created this Lecturer
}
