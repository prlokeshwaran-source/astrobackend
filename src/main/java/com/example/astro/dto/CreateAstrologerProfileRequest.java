package com.example.astro.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateAstrologerProfileRequest(
        @NotBlank @Size(max = 120) String name,
        @NotNull @PositiveOrZero Integer experienceYears,
        @NotBlank @Size(max = 500) String specialization,
        @Size(max = 2048) String imageUrl,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal pricePerHour) {}
