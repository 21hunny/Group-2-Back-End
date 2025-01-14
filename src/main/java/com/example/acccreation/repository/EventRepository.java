package com.example.acccreation.repository;

import com.example.acccreation.entity.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    @Query("SELECT e FROM Event e WHERE e.lId = :lId AND e.name = :name")
    List<Event> findByLIdAndName(@Param("lId") String lId, @Param("name") String name);

    @Query("SELECT e FROM Event e WHERE e.lId IS NULL AND e.name = :eventName AND e.sId = :studentId")
    List<Event> findEventsByStudentId(@Param("eventName") String eventName, @Param("studentId") String studentId);

    @Query("SELECT e FROM Event e WHERE e.name = :name")
    List<Event> findByName(@Param("name") String name);

}
