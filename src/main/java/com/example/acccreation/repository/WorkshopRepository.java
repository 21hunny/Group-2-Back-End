package com.example.acccreation.repository;

import com.example.acccreation.entity.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, String> {
    @Query("SELECT w FROM Workshop w WHERE w.event.id = :eId")
    Workshop findByEId(@Param("eId") String eId);


    @Query("SELECT w.event.id FROM Workshop w WHERE w.wId = :workshopId")
    String findEventIdByWorkshopId(@Param("workshopId") String workshopId);

}

