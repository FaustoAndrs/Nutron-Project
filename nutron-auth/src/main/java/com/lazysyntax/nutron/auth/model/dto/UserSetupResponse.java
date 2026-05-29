package com.lazysyntax.nutron.auth.model.dto;

import lombok.Builder;

@Builder
public record UserSetupResponse(
        String weight,
        String height,
        String gender,
        String age,
        String activity,
        String goal,
        String formula,
        String diet
) {

}
