package com.example.astro.controller;

import com.example.astro.dto.LoginRequest;
import com.example.astro.dto.RegisterRequest;
import com.example.astro.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        AuthService.RegistrationResult result = authService.register(request);
        return ResponseEntity.status(result.created() ? HttpStatus.CREATED : HttpStatus.CONFLICT)
                .body(result.body());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        AuthService.LoginResult result = authService.login(request);
        return ResponseEntity.status(result.authenticated() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED)
                .body(result.body());
    }
}
