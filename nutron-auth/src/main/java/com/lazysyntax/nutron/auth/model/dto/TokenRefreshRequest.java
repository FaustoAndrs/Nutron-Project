package com.lazysyntax.nutron.auth.model.dto;

public record TokenRefreshRequest(
    String refreshToken
) {}
