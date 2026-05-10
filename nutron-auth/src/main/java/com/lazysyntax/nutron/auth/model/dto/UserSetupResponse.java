package com.lazysyntax.nutron.auth.model.dto;

import com.lazysyntax.nutron.auth.model.UserSetup;

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
    public static UserSetupResponse fromEntity(UserSetup entity) {
        if (entity == null) return null;
        return new UserSetupResponse(
            entity.getWeight(),
            entity.getHeight(),
            entity.getGender(),
            entity.getAge(),
            entity.getActivity(),
            entity.getGoal(),
            entity.getFormula(),
                entity.getDiet()
        );
    }
}
