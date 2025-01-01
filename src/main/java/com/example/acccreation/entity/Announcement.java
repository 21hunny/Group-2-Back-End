package com.example.acccreation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "announcement")
public class Announcement {

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Id
    @Column(name = "a_id", nullable = false, unique = true, length = 50)
    private String aId;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @ManyToOne
    @JoinColumn(name = "e_id", nullable = false)
    private Event event;
}

