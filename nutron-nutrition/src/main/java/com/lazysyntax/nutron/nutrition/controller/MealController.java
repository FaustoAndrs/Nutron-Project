package com.lazysyntax.nutron.nutrition.controller;

import com.lazysyntax.nutron.nutrition.model.dto.MealRequest.MealDto;
import com.lazysyntax.nutron.nutrition.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/meals")
public class MealController {

    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public ResponseEntity<MealDto> createMeal(@RequestBody MealDto mealDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            userId = (String) authentication.getPrincipal();
            mealDto.setUserId(userId); // Set the userId from the authenticated user
        } else {
            // Handle case where user is not authenticated, though JwtRequestFilter should prevent this
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        // Use the service to create the meal
        MealDto createdMeal = mealService.createMeal(mealDto);

        return ResponseEntity.ok(createdMeal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealDto> updateMeal(@PathVariable String id, @RequestBody MealDto mealDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            userId = (String) authentication.getPrincipal();
            // Ensure the meal being updated belongs to the authenticated user, or handle authorization
            // For now, we'll just set the userId in the DTO if it's not already set,
            // but a more robust solution would involve checking ownership in the service layer.
            if (mealDto.getUserId() == null) {
                mealDto.setUserId(userId);
            }
        } else {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        MealDto updatedMeal = mealService.updateMeal(id, mealDto);

        if (updatedMeal != null) {
            return ResponseEntity.ok(updatedMeal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Example of a public endpoint (no authentication required)
    @GetMapping("/public/hello")
    public ResponseEntity<String> publicHello() {
        return ResponseEntity.ok("Hello from public endpoint!");
    }
}
