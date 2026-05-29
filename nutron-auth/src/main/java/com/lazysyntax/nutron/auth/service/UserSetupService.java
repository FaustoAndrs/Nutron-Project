package com.lazysyntax.nutron.auth.service;

import com.lazysyntax.nutron.auth.model.entity.User;
import com.lazysyntax.nutron.auth.model.entity.UserSetup;
import com.lazysyntax.nutron.auth.model.dto.UserSetupRequest;
import com.lazysyntax.nutron.auth.model.dto.UserSetupResponse;
import com.lazysyntax.nutron.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lazysyntax.nutron.auth.converter.UserSetupConverter.*;

@Service
@RequiredArgsConstructor
public class UserSetupService {

    private final UserRepository userRepository;

    @Transactional
    public UserSetupResponse saveSetup(String userUuid, UserSetupRequest request) {

        //Se comprueba que existe el usuario
        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        UserSetup setup = user.getUserSetup();
        if (setup == null) {
            setup = toEntity(request, user); // Cuando el usuario configura sus parametros
            user.setUserSetup(setup);
        } else {
            setup = toEntity(request); // Cuando el usuario actualiza sus parametros
        }

        // Guardamos en el usuario el setup.
        User savedUser = userRepository.save(user);

        //UserSetup mapeado como Response
        return toResponse(savedUser.getUserSetup());
    }

    public UserSetupResponse getSetup(String userIdStr) {

        User user = userRepository
                .findById(userIdStr)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (user.getUserSetup() == null) {
            return null;
        }

        return toResponse(user.getUserSetup());
    }

    @Transactional
    public UserSetupResponse updateDiet(String userIdStr, String newDiet) {

        User user = userRepository
                .findById(userIdStr)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserSetup setup = user.getUserSetup();
        if (setup == null) {
            throw new RuntimeException("UserSetup no encontrado para el usuario: " + userIdStr);
        }

        setup.setDiet(newDiet);
        userRepository.save(user);
        return toResponse(setup);
    }
}