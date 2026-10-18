package com.example.astro.service;

import com.example.astro.dto.AuthResponse;
import com.example.astro.dto.LoginRequest;
import com.example.astro.dto.MessageResponse;
import com.example.astro.dto.RegisterRequest;
import com.example.astro.model.User;
import com.example.astro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminPassword;
    private final String astrologerEmail;
    private final String astrologerPassword;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.auth.admin.email}") String adminEmail,
            @Value("${app.auth.admin.password}") String adminPassword,
            @Value("${app.auth.astrologer.email}") String astrologerEmail,
            @Value("${app.auth.astrologer.password}") String astrologerPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = normalize(adminEmail);
        this.adminPassword = adminPassword;
        this.astrologerEmail = normalize(astrologerEmail);
        this.astrologerPassword = astrologerPassword;
    }

    public RegistrationResult register(RegisterRequest request) {
        String email = normalize(request.email());
        if (email.equals(adminEmail) || email.equals(astrologerEmail) || userRepository.existsByEmail(email)) {
            return new RegistrationResult(false, new MessageResponse("This email is already in use"));
        }
        try {
            userRepository.save(new User(email, passwordEncoder.encode(request.password())));
        } catch (DataIntegrityViolationException exception) {
            return new RegistrationResult(false, new MessageResponse("This email is already in use"));
        }
        return new RegistrationResult(true, new AuthResponse("Registration successful", "USER", email));
    }

    public LoginResult login(LoginRequest request) {
        String email = normalize(request.email());
        String role;
        boolean valid;

        if (email.equals(adminEmail)) {
            role = "ADMIN";
            valid = adminPassword.equals(request.password());
        } else if (email.equals(astrologerEmail)) {
            role = "ASTROLOGER";
            valid = astrologerPassword.equals(request.password());
        } else {
            role = "USER";
            User user = userRepository.findByEmail(email).orElse(null);
            valid = user != null && passwordEncoder.matches(request.password(), user.getPasswordHash());
        }

        return valid
                ? new LoginResult(true, new AuthResponse("Login successful", role, email))
                : new LoginResult(false, new MessageResponse("Invalid email or password"));
    }

    private static String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public record RegistrationResult(boolean created, Object body) {}
    public record LoginResult(boolean authenticated, Object body) {}
}
