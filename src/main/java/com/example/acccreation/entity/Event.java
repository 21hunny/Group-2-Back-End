package com.example.acccreation.entity;

import jakarta.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private Date date;

    @Column(name = "time")
    private Time time;

    @Column(name = "status")
    private String status;

    @Column(name = "l_id")
    private String lId;

    @Column(name = "s_id", nullable = true) // Nullable for batch events
    private String sId;

    @Column(name = "b_id", nullable = true) // Nullable for individual events
    private String bId;


    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLId() {
        return lId;
    }

    public void setLId(String lId) {
        this.lId = lId;
    }

    public String getSId() {
        return sId;
    }

    public void setSId(String sId) {
        this.sId = sId;
    }

    public String getBId() {
        return bId;
    }

    public void setBId(String bId) {
        this.bId = bId;
    }
}
