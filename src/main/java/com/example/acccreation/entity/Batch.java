package com.example.acccreation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "batch")
public class Batch {

    @Id
    private String id;  // e.g. "B2024" or "gadse232f"

    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    private String department;
    private String course;

    @ManyToOne
    @JoinColumn(name = "a_id")
    private Admin admin;  // The Admin who created this batch
}
