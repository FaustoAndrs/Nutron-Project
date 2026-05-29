package com.lazysyntax.nutron.auth.model.dto;

import com.lazysyntax.nutron.auth.converter.UserSetupConverter;
import com.lazysyntax.nutron.auth.model.entity.User;
import lombok.Builder;

@Builder
public record UserResponse(
    String id,
    String userName,
    String fullName,
    String email,
    UserSetupResponse userSetup
) {
    public static UserResponse toResponse(User entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .userName(entity.getUserName())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .userSetup(UserSetupConverter.toResponse(entity.getUserSetup()))
                .build();
    }
}
