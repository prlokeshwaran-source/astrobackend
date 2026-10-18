package com.example.astro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "astrologer_profiles")
public class AstrologerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false)
    private Integer experienceYears;

    @Column(nullable = false, length = 500)
    private String specialization;

    @Column(length = 2048)
    private String imageUrl;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    protected AstrologerProfile() {}

    public AstrologerProfile(String name, Integer experienceYears, String specialization,
                             String imageUrl, BigDecimal pricePerHour) {
        this.name = name;
        this.experienceYears = experienceYears;
        this.specialization = specialization;
        this.imageUrl = imageUrl;
        this.pricePerHour = pricePerHour;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getExperienceYears() { return experienceYears; }
    public String getSpecialization() { return specialization; }
    public String getImageUrl() { return imageUrl; }
    public BigDecimal getPricePerHour() { return pricePerHour; }
}
