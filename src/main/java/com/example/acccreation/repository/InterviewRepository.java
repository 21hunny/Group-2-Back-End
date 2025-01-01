package com.example.acccreation.repository;

import com.example.acccreation.entity.Announcement;
import com.example.acccreation.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, String> {
    @Query("SELECT i FROM Interview i WHERE i.event.id = :eId")
    Interview findByEId(@Param("eId") String eId);

    @Query("SELECT i.event.id FROM Interview i WHERE i.iId = :interviewId")
    String findEventIdByInterviewId(@Param("interviewId") String interviewId);
}

