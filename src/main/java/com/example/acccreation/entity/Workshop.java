package com.example.acccreation.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "workshop")
public class Workshop {

    public String getwId() {
        return wId;
    }

    public void setwId(String wId) {
        this.wId = wId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Id
    @Column(name = "w_id", nullable = false, unique = true, length = 50)
    private String wId;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "contact", nullable = false, length = 50)
    private String contact;

    @ManyToOne
    @JoinColumn(name = "e_id", nullable = false)
    private Event event;
}

