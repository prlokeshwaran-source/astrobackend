package com.example.astro.service;

import com.example.astro.dto.AstrologyReadingRequest;
import com.example.astro.dto.AstrologyReadingResponse;
import com.example.astro.dto.DashaPeriod;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

@Service
public class AstrologyReadingService {
    private final ObjectProvider<ChatClient.Builder> chatClientBuilder;
    private final VimshottariDashaService dashaService;
    private final String apiKey;

    public AstrologyReadingService(ObjectProvider<ChatClient.Builder> chatClientBuilder,
                                   VimshottariDashaService dashaService,
                                   @Value("${spring.ai.openai.api-key:}") String apiKey) {
        this.chatClientBuilder = chatClientBuilder;
        this.dashaService = dashaService;
        this.apiKey = apiKey;
    }

    public AstrologyReadingResponse generate(AstrologyReadingRequest request) {
        ZoneId zone;
        try {
            zone = ZoneId.of(request.timeZone());
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("timeZone must be a valid IANA time-zone ID, such as Asia/Kolkata");
        }
        List<DashaPeriod> periods = dashaService.calculate(request);
        if (apiKey == null || apiKey.isBlank()) {
            throw new AiNotConfiguredException("AI prediction is not configured. Set the OPENAI_API_KEY environment variable.");
        }
        ChatClient.Builder builder = chatClientBuilder.getIfAvailable();
        if (builder == null) {
            throw new AiNotConfiguredException("Spring AI chat model is unavailable. Check the OpenAI configuration.");
        }

        String prediction = builder.build().prompt()
                .system("You are a culturally respectful Vedic astrology assistant. Give a clear, practical, non-deterministic reading. Frame astrology as reflection, never as certain fact. Do not give medical, legal, or financial directives.")
                .user(user -> user.text("Write a concise Vedic astrology reading using this supplied chart context. Do not calculate or change the dasha dates. Birth date: {birthDate}; birth time: {birthTime}; time zone: {timeZone}; Moon nakshatra: {nakshatra}; nakshatra elapsed fraction: {progress}; requested focus: {focus}. Mention that results depend on the supplied birth details.")
                        .param("birthDate", request.birthDate())
                        .param("birthTime", request.birthTime())
                        .param("timeZone", zone.getId())
                        .param("nakshatra", request.nakshatra())
                        .param("progress", request.nakshatraProgress())
                        .param("focus", request.focus() == null || request.focus().isBlank() ? "general life themes" : request.focus()))
                .call().content();
        return new AstrologyReadingResponse(prediction, periods);
    }

    public static class AiNotConfiguredException extends RuntimeException {
        public AiNotConfiguredException(String message) { super(message); }
    }
}
