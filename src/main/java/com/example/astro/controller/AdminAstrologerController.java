package com.example.astro.controller;

import com.example.astro.dto.CreateAstrologerProfileRequest;
import com.example.astro.model.AstrologerProfile;
import com.example.astro.service.AstrologerProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/astrologers")
public class AdminAstrologerController {
    private final AstrologerProfileService profileService;

    public AdminAstrologerController(AstrologerProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AstrologerProfile create(@Valid @RequestBody CreateAstrologerProfileRequest request) {
        return profileService.create(request);
    }
}
