package com.lazysyntax.nutron.auth.model.dto;

public record LoginRequest(
    String email,
    String password
) {}
