package com.example.astro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CreateChatRoomRequest(@Email @NotNull String userEmail, @NotNull Long astrologerProfileId) {}
