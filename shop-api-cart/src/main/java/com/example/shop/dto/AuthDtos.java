package com.example.shop.dto;

import jakarta.validation.constraints.*;

public class AuthDtos {
    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record RegisterRequest(
            @NotBlank @Size(min=4,max=30) String username,
            @Email String email,
            @NotBlank @Size(min=6) String password,
            String fullName
    ) {}

    public record AuthResponse(String token, String username, String role) {}
}
