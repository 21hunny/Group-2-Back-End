package com.example.acccreation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin")
public class Admin {

    @Id
    private String id; // e.g. "A001"

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "login_attempt")
    private int loginAttempt;

    @Column(name = "status")
    private String status;
}
