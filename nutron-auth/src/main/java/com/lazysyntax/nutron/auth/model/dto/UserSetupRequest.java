package com.lazysyntax.nutron.auth.model.dto;

public record UserSetupRequest(
    String weight,
    String height,
    String gender,
    String age,
    String activity,
    String goal,
    String formula,
    String diet

) {}
