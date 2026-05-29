package com.lazysyntax.nutron.nutrition.controller;

import com.lazysyntax.nutron.nutrition.model.dto.MealDto;
import com.lazysyntax.nutron.nutrition.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        // Si el usuario esta autorizado y validado, se extrae el Uuid y se vincula al nuevo registro "Meal".
        if (authentication != null && authentication.isAuthenticated()) {
            userId = (String) authentication.getPrincipal();
            mealDto.setUserId(userId);
        } else {
            return ResponseEntity.status(401).build();
        }

        MealDto createdMeal = mealService.createMeal(mealDto);

        return ResponseEntity.ok(createdMeal);
    }

    @GetMapping("/me") // Endpoint para obtener las comidas "Meals" del usuario autenticado
    public ResponseEntity<List<MealDto>> getMealsForAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = null;

        if (authentication != null && authentication.isAuthenticated()) {
            userId = (String) authentication.getPrincipal();
        } else {
            return ResponseEntity.status(401).build();
        }

        List<MealDto> meals = mealService.getMealsByUserId(userId);

        if (meals != null && !meals.isEmpty()) {
            return ResponseEntity.ok(meals);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
