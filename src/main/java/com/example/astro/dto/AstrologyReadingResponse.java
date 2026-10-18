package com.example.astro.dto;

import java.util.List;

public record AstrologyReadingResponse(String prediction, List<DashaPeriod> dashaBhuktiTable) {
}
