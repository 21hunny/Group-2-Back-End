package com.example.acccreation.repository;

import com.example.acccreation.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    @Query("SELECT t.lecturerId FROM Team t WHERE t.leaderId = :leaderId")
    List<String> findLecturerIdsByLeaderId(String leaderId);

    @Query("SELECT t.id FROM Team t WHERE t.leaderId = :studentId OR t.members LIKE CONCAT('%', :studentId, '%')")
    String findTeamIdByStudentId(String studentId);

    @Query("SELECT MAX(t.id) FROM Team t")
    String findMaxId();

    List<Team> findAllByBatchId(String batchId);
}


