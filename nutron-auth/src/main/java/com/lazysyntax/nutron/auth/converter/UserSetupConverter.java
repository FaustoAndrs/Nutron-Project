package com.lazysyntax.nutron.auth.converter;

import com.lazysyntax.nutron.auth.model.dto.UserSetupRequest;
import com.lazysyntax.nutron.auth.model.dto.UserSetupResponse;
import com.lazysyntax.nutron.auth.model.entity.User;
import com.lazysyntax.nutron.auth.model.entity.UserSetup;
import org.springframework.stereotype.Component;

@Component
public class UserSetupConverter {

    public static UserSetup toEntity(UserSetupRequest request, User user) {
        return UserSetup.builder()
                .user(user)
                .weight(request.weight())
                .height(request.height())
                .gender(request.gender())
                .age(request.age())
                .activity(request.activity())
                .goal(request.goal())
                .formula(request.formula())
                .diet(request.diet())
                .build();
    }

    public static UserSetup toEntity(UserSetupRequest request, UserSetup existingSetup) {
        return UserSetup.builder()
                .weight(request.weight())
                .height(request.height())
                .gender(request.gender())
                .age(request.age())
                .activity(request.activity())
                .goal(request.goal())
                .formula(request.formula())
                .diet(request.diet())
                .build();
    }

    public static UserSetupResponse toResponse(UserSetup entity) {
        if (entity == null) {
            return null;
        }
        return UserSetupResponse.builder()
                .weight(entity.getWeight())
                .height(entity.getHeight())
                .gender(entity.getGender())
                .age(entity.getAge())
                .activity(entity.getActivity())
                .goal(entity.getGoal())
                .formula(entity.getFormula())
                .diet(entity.getDiet())
                .build();

    }
}
