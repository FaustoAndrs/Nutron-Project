package com.lazysyntax.nutron.auth.service;

import com.lazysyntax.nutron.auth.model.User;
import com.lazysyntax.nutron.auth.model.UserSetup;
import com.lazysyntax.nutron.auth.model.dto.UserSetupRequest;
import com.lazysyntax.nutron.auth.model.dto.UserSetupResponse;
import com.lazysyntax.nutron.auth.repository.UserRepository;
import com.lazysyntax.nutron.auth.repository.UserSetupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSetupService {

    private final UserSetupRepository userSetupRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserSetupResponse saveOrUpdateSetup(String userIdStr, UserSetupRequest request) {
        Long userId = Long.parseLong(userIdStr);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserSetup setup = user.getUserSetup();
        if (setup == null) {
            setup = UserSetup.builder()
                    .user(user)
                    .weight(request.weight())
                    .height(request.height())
                    .gender(request.gender())
                    .age(request.age())
                    .activity(request.activity())
                    .goal(request.goal())
                    .formula(request.formula())
                    .diet(request.diet()) // Added diet
                    .build();
            user.setUserSetup(setup);
        } else {
            setup.setWeight(request.weight());
            setup.setHeight(request.height());
            setup.setGender(request.gender());
            setup.setAge(request.age());
            setup.setActivity(request.activity());
            setup.setGoal(request.goal());
            setup.setFormula(request.formula());
            setup.setDiet(request.diet()); // Added diet
        }

        // Guardamos el usuario, que por cascada guardará el setup
        User savedUser = userRepository.save(user);
        return UserSetupResponse.fromEntity(savedUser.getUserSetup());
    }

    public UserSetupResponse getSetup(String userIdStr) {
        Long userId = Long.parseLong(userIdStr);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (user.getUserSetup() == null) {
            return null;
        }

        return UserSetupResponse.fromEntity(user.getUserSetup());
    }

    @Transactional
    public UserSetupResponse updateDiet(String userIdStr, String newDiet) {
        Long userId = Long.parseLong(userIdStr);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserSetup setup = user.getUserSetup();
        if (setup == null) {
            throw new RuntimeException("UserSetup no encontrado para el usuario: " + userIdStr);
        }

        setup.setDiet(newDiet);
        userRepository.save(user); // Save user, which cascades to UserSetup
        return UserSetupResponse.fromEntity(setup);
    }



}
