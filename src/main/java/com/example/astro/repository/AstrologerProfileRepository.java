package com.example.astro.repository;

import com.example.astro.model.AstrologerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AstrologerProfileRepository extends JpaRepository<AstrologerProfile, Long> {}
