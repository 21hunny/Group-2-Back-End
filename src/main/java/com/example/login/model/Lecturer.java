package com.example.login.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecturer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Lecturer {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id; // Lecturer ID (String)

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "department")
    private String department;

    @Column(name = "contact")
    private String contact;

    @Column(name = "course_assign")
    private String courseAssign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "a_id", referencedColumnName = "id")
    private Admin admin; // Foreign Key to Admin table
}
