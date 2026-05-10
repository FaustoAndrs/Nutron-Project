package com.lazysyntax.nutron.auth.model.dto;

import com.lazysyntax.nutron.auth.model.User;

public record UserResponse(
    Long id,
    String userName,
    String fullName,
    String email,
    UserSetupResponse userSetup
) {
    public static UserResponse fromEntity(User entity) {
        return new UserResponse(
            entity.getId(),
            entity.getUserName(),
            entity.getFullName(),
            entity.getEmail(),
            UserSetupResponse.fromEntity(entity.getUserSetup())
        );
    }
}
