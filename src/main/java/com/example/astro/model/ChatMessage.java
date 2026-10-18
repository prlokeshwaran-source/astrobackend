package com.example.astro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 36)
    private String roomCode;

    @Column(nullable = false, length = 254)
    private String senderEmail;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false, updatable = false)
    private Instant sentAt;

    protected ChatMessage() {}

    public ChatMessage(String roomCode, String senderEmail, String content) {
        this.roomCode = roomCode;
        this.senderEmail = senderEmail;
        this.content = content;
        this.sentAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getRoomCode() { return roomCode; }
    public String getSenderEmail() { return senderEmail; }
    public String getContent() { return content; }
    public Instant getSentAt() { return sentAt; }
}
