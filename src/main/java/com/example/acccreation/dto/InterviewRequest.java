package com.example.acccreation.dto;

import lombok.Data;

@Data
public class InterviewRequest {
    private String companyName; // Name of the company conducting the interview
    private String position;    // Position offered in the interview
    private String mode;        // Mode of the interview (e.g., Online, In-person)
}
