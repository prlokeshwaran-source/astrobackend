package com.example.astro.service;

import com.example.astro.dto.CreateAstrologerProfileRequest;
import com.example.astro.model.AstrologerProfile;
import com.example.astro.repository.AstrologerProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class AstrologerProfileService {
    private final AstrologerProfileRepository repository;

    public AstrologerProfileService(AstrologerProfileRepository repository) {
        this.repository = repository;
    }

    public AstrologerProfile create(CreateAstrologerProfileRequest request) {
        AstrologerProfile profile = new AstrologerProfile(
                request.name().trim(), request.experienceYears(), request.specialization().trim(),
                request.imageUrl(), request.pricePerHour());
        return repository.save(profile);
    }
}
