package com.example.acccreation.dto;

import lombok.Data;

@Data
public class PortfolioRequest {
    private String name;      // Portfolio name
    private String about;     // About section
    private String projects;  // Projects included in the portfolio
    private String photo;     // Photo URL or binary data
    private String bio;       // Biography or description
}
