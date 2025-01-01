package com.example.acccreation.repository;

import com.example.acccreation.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Query("SELECT MAX(m.id) FROM Message m")
    String findMaxId();

    @Query("SELECT m FROM Message m WHERE m.receiverId = :lecturerId ORDER BY m.date DESC")
    List<Message> findMessagesByReceiverId(@Param("lecturerId") String lecturerId);

}
