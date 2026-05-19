package com.lazysyntax.nutron.nutrition.controller;

import com.lazysyntax.nutron.nutrition.model.dto.MealRequest.FoodDto;
import com.lazysyntax.nutron.nutrition.model.entity.Food;
import com.lazysyntax.nutron.nutrition.service.FoodService;
import com.lazysyntax.nutron.nutrition.util.FoodConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PutMapping("/{id}")
    public ResponseEntity<FoodDto> updateFood(@PathVariable String id, @RequestBody FoodDto foodDto) {
        Food updatedFood = foodService.updateFood(id, foodDto);
        if (updatedFood != null) {
            return ResponseEntity.ok(FoodConverters.toDto(updatedFood));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable String id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDto> getFoodById(@PathVariable String id) {
        Food food = foodService.getFoodById(id);
        if (food != null) {
            return ResponseEntity.ok(FoodConverters.toDto(food));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<FoodDto>> getAllFoods() {
        List<Food> foods = foodService.getAllFoods();
        List<FoodDto> foodDtos = foods.stream()
                .map(FoodConverters::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodDtos);
    }
}