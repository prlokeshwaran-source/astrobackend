package com.example.astro.repository;

import com.example.astro.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomCodeOrderBySentAtAsc(String roomCode);
}
