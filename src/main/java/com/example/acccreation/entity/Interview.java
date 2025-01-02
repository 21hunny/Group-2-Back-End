package com.example.acccreation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interview")
public class Interview {

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getiId() {
        return iId;
    }

    public void setiId(String iId) {
        this.iId = iId;
    }

    @Id
    @Column(name = "i_id", nullable = false, unique = true, length = 50)
    private String iId;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "position", nullable = false, length = 100)
    private String position;

    @Column(name = "mode", nullable = false, length = 50)
    private String mode;

    @ManyToOne
    @JoinColumn(name = "e_id", nullable = false)
    private Event event;
}

