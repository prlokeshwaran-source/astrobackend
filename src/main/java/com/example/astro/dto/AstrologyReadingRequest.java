package com.example.astro.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record AstrologyReadingRequest(
        @NotNull LocalDate birthDate,
        @NotNull LocalTime birthTime,
        @NotBlank String timeZone,
        @NotBlank String nakshatra,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") Double nakshatraProgress,
        @Size(max = 200) String focus) {
}
