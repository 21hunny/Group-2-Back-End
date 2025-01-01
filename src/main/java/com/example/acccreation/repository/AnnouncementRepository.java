package com.example.acccreation.repository;

import com.example.acccreation.entity.Announcement;
import com.example.acccreation.entity.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, String> {
    @Query("SELECT a FROM Announcement a WHERE a.event.id = :eId")
    Announcement findByEId(@Param("eId") String eId);

    @Query("SELECT a.event.id FROM Announcement a WHERE a.aId = :announcementId")
    String findEventIdByAnnouncementId(@Param("announcementId") String announcementId);

}

