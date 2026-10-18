package com.example.astro.dto;

import java.time.LocalDate;

public record DashaPeriod(String mahadasha, String bhukti, LocalDate startDate, LocalDate endDate) {
}
