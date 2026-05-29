package com.lazysyntax.nutron.nutrition.controller;

import com.lazysyntax.nutron.nutrition.model.dto.FoodDto;
import com.lazysyntax.nutron.nutrition.model.entity.Food;
import com.lazysyntax.nutron.nutrition.service.FoodService;
import com.lazysyntax.nutron.nutrition.converter.FoodConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/foods")
public class FoodController {

    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public ResponseEntity<FoodDto> createFood(@RequestBody FoodDto foodDto) {
        Food createdFood = foodService.createFood(foodDto);
        return new ResponseEntity<>(FoodConverters.toDto(createdFood), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<List<FoodDto>> getFoodsForAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId;
        if (authentication != null && authentication.isAuthenticated()) {
            userId = (String) authentication.getPrincipal();
        } else {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        List<FoodDto> foods = foodService.getFoodsByUserId(userId);
        if(foods != null && !foods.isEmpty()) {
            return ResponseEntity.ok(foods);
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }
}