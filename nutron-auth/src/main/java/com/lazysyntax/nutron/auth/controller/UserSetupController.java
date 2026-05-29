package com.lazysyntax.nutron.auth.controller;

import com.lazysyntax.nutron.auth.model.dto.UserSetupDietUpdateRequest;
import com.lazysyntax.nutron.auth.model.dto.UserSetupRequest;
import com.lazysyntax.nutron.auth.model.dto.UserSetupResponse;
import com.lazysyntax.nutron.auth.service.UserSetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/setup")
@RequiredArgsConstructor
public class UserSetupController {

    private final UserSetupService userSetupService;

    @PutMapping
    public ResponseEntity<UserSetupResponse> saveSetup(
            @AuthenticationPrincipal String userId,
            @RequestBody UserSetupRequest request
    ) {
        return ResponseEntity.ok(userSetupService.saveSetup(userId, request));
    }

    @GetMapping
    public ResponseEntity<UserSetupResponse> getSetup(
            @AuthenticationPrincipal String userId
    ) {
        UserSetupResponse setup = userSetupService.getSetup(userId);
        if (setup == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(setup);
    }

    @PatchMapping("/diet")
    public ResponseEntity<UserSetupResponse> updateDiet(
            @AuthenticationPrincipal String userId,
            @RequestBody UserSetupDietUpdateRequest request
    ) {
        if (request.diet() == null || request.diet().isBlank()) {
            // Manejar el error, por ejemplo, lanzar una excepción o devolver un Bad Request
            return ResponseEntity.badRequest().body(null); // O un ErrorResponse
        }
        return ResponseEntity.ok(userSetupService.updateDiet(userId, request.diet()));
    }



}
