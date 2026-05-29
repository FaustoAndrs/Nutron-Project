package com.lazysyntax.nutron.nutrition.service;

import com.lazysyntax.nutron.nutrition.model.dto.MealDto;
import com.lazysyntax.nutron.nutrition.model.entity.Meal;
import com.lazysyntax.nutron.nutrition.repository.MealRepository;
import com.lazysyntax.nutron.nutrition.converter.MealConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lazysyntax.nutron.nutrition.converter.MealConverters.toEntity;

@Service
public class MealService {

    private final MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Transactional
    public MealDto createMeal(MealDto mealDto) {
        Meal mealEntity = MealConverters.toEntity(mealDto);
        Meal savedMeal = mealRepository.save(mealEntity);

        return MealConverters.toDto(savedMeal);
    }

    @Transactional(readOnly = true)
    public List<MealDto> getMealsByUserId(String userId) {
        return mealRepository.findAllByUserId(userId).stream().map(MealConverters::toDto).toList();

    }
}
