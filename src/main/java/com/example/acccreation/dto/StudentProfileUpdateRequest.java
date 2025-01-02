package com.example.acccreation.dto;

import lombok.Data;

/**
 * DTO for updating student profile information.
 */
@Data
public class StudentProfileUpdateRequest {
    private String contact;
    private String email;
    private String name;
    private String photo;
}
