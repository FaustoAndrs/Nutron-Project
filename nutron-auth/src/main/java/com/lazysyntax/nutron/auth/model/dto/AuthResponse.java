package com.lazysyntax.nutron.auth.model.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken
) {}
