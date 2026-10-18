package com.example.astro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String roomCode;

    @Column(nullable = false, length = 254)
    private String userEmail;

    @Column(nullable = false)
    private Long astrologerProfileId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected ChatRoom() {}

    public ChatRoom(String userEmail, Long astrologerProfileId) {
        this.roomCode = UUID.randomUUID().toString();
        this.userEmail = userEmail;
        this.astrologerProfileId = astrologerProfileId;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getRoomCode() { return roomCode; }
    public String getUserEmail() { return userEmail; }
    public Long getAstrologerProfileId() { return astrologerProfileId; }
    public Instant getCreatedAt() { return createdAt; }
}
