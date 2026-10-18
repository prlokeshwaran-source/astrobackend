package com.example.astro.service;

import com.example.astro.dto.AstrologyReadingRequest;
import com.example.astro.dto.DashaPeriod;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Calculates Vimshottari periods from a supplied birth nakshatra and its elapsed fraction. */
@Service
public class VimshottariDashaService {
    private static final List<String> LORDS = List.of(
            "Ketu", "Venus", "Sun", "Moon", "Mars", "Rahu", "Jupiter", "Saturn", "Mercury");
    private static final Map<String, Integer> YEARS = Map.of(
            "Ketu", 7, "Venus", 20, "Sun", 6, "Moon", 10, "Mars", 7,
            "Rahu", 18, "Jupiter", 16, "Saturn", 19, "Mercury", 17);
    private static final List<String> NAKSHATRAS = List.of(
            "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra", "Punarvasu",
            "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni", "Hasta",
            "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha", "Mula", "Purva Ashadha",
            "Uttara Ashadha", "Shravana", "Dhanishta", "Shatabhisha", "Purva Bhadrapada",
            "Uttara Bhadrapada", "Revati");

    public List<DashaPeriod> calculate(AstrologyReadingRequest request) {
        String normalizedNakshatra = normalize(request.nakshatra());
        int nakshatraIndex = -1;
        for (int index = 0; index < NAKSHATRAS.size(); index++) {
            if (normalize(NAKSHATRAS.get(index)).equals(normalizedNakshatra)) {
                nakshatraIndex = index;
                break;
            }
        }
        if (nakshatraIndex < 0) {
            throw new IllegalArgumentException("nakshatra must be one of the 27 standard nakshatra names");
        }
        int firstLordIndex = nakshatraIndex % LORDS.size();
        String firstLord = LORDS.get(firstLordIndex);
        double elapsed = request.nakshatraProgress();
        LocalDate birth = request.birthDate();
        double firstRemaining = YEARS.get(firstLord) * (1.0 - elapsed);
        LocalDate mahaStart = addYears(birth, -YEARS.get(firstLord) * elapsed);
        List<DashaPeriod> periods = new ArrayList<>();

        for (int mahaOffset = 0; mahaOffset < LORDS.size(); mahaOffset++) {
            int mahaIndex = (firstLordIndex + mahaOffset) % LORDS.size();
            String mahaLord = LORDS.get(mahaIndex);
            double mahaYears = YEARS.get(mahaLord);
            LocalDate mahaEnd = mahaOffset == 0
                    ? addYears(birth, firstRemaining)
                    : addYears(mahaStart, mahaYears);

            // Bhukti durations within a Mahadasha use the standard lord-year ratio.
            LocalDate bhuktiStart = mahaStart;
            for (int bhuktiOffset = 0; bhuktiOffset < LORDS.size(); bhuktiOffset++) {
                String bhuktiLord = LORDS.get((mahaIndex + bhuktiOffset) % LORDS.size());
                double bhuktiYears = YEARS.get(mahaLord) * YEARS.get(bhuktiLord) / 120.0;
                LocalDate bhuktiEnd = addYears(bhuktiStart, bhuktiYears);
                if (bhuktiEnd.isAfter(birth) && bhuktiStart.isBefore(mahaEnd)) {
                    periods.add(new DashaPeriod(mahaLord, bhuktiLord,
                            bhuktiStart.isBefore(birth) ? birth : bhuktiStart,
                            bhuktiEnd.isAfter(mahaEnd) ? mahaEnd : bhuktiEnd));
                }
                bhuktiStart = bhuktiEnd;
            }
            mahaStart = mahaOffset == 0 ? addYears(birth, firstRemaining) : mahaEnd;
        }
        return List.copyOf(periods);
    }

    private static LocalDate addYears(LocalDate start, double years) {
        long days = Math.round(years * 365.2425);
        return start.plusDays(days);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("\\s+", " ");
    }
}
