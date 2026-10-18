package com.example.astro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatMessageRequest(@Email @NotBlank String senderEmail, @NotBlank @Size(max = 2000) String content) {}
