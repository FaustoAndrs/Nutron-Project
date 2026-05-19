package com.lazysyntax.nutron.nutrition.service;

import com.lazysyntax.nutron.nutrition.model.dto.MealRequest.MealDto;
import com.lazysyntax.nutron.nutrition.model.entity.Meal;
import com.lazysyntax.nutron.nutrition.repository.MealRepository;
import com.lazysyntax.nutron.nutrition.util.MealConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.lazysyntax.nutron.nutrition.util.MealConverters.convertToEntity;

@Service
public class MealService {

    private final MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Transactional
    public MealDto createMeal(MealDto mealDto) {
        // Convert MealDto to Meal entity
        // The convertToEntity method in MealConverters no longer sets the ID,
        // allowing the database to generate it for new entities.
        Meal meal = convertToEntity(mealDto);

        // Save the entity
        Meal savedMeal = mealRepository.save(meal);

        // Convert the saved entity back to DTO for a consistent response
        return MealConverters.convertToDto(savedMeal);
    }

    @Transactional
    public MealDto updateMeal(String mealId, MealDto mealDto) {
        Optional<Meal> existingMealOptional = mealRepository.findById(mealId);

        if (existingMealOptional.isPresent()) {
            Meal existingMeal = existingMealOptional.get();
            // Update the existing entity with data from the DTO
            MealConverters.updateEntity(mealDto, existingMeal);
            Meal updatedMeal = mealRepository.save(existingMeal);
            return MealConverters.convertToDto(updatedMeal);
        } else {
            // Meal not found, you might want to throw a specific exception
            return null;
        }
    }

    // You might also want methods for getMealById, getAllMeals, deleteMeal
    // For example:
    @Transactional(readOnly = true)
    public MealDto getMealById(String mealId) {
        return mealRepository.findById(mealId)
                .map(MealConverters::convertToDto)
                .orElse(null);
    }

    @Transactional
    public void deleteMeal(String mealId) {
        mealRepository.deleteById(mealId);
    }
}
