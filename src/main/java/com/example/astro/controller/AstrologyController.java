package com.example.astro.controller;

import com.example.astro.dto.AstrologyReadingRequest;
import com.example.astro.dto.AstrologyReadingResponse;
import com.example.astro.service.AstrologyReadingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RequestMapping("/api/astrology")
public class AstrologyController {
    private final AstrologyReadingService readingService;

    public AstrologyController(AstrologyReadingService readingService) {
        this.readingService = readingService;
    }

    @PostMapping("/reading")
    public AstrologyReadingResponse generate(@Valid @RequestBody AstrologyReadingRequest request) {
        return readingService.generate(request);
    }

    @RestControllerAdvice
    static class AstrologyErrorHandler {
        @ExceptionHandler(IllegalArgumentException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ErrorResponse badRequest(IllegalArgumentException exception) {
            return new ErrorResponse(exception.getMessage());
        }

        @ExceptionHandler(AstrologyReadingService.AiNotConfiguredException.class)
        @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
        public ErrorResponse unavailable(AstrologyReadingService.AiNotConfiguredException exception) {
            return new ErrorResponse(exception.getMessage());
        }
    }

    public record ErrorResponse(String message) { }
}
