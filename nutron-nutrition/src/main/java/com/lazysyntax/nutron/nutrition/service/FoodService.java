package com.lazysyntax.nutron.nutrition.service;

import com.lazysyntax.nutron.nutrition.model.dto.FoodDto;
import com.lazysyntax.nutron.nutrition.model.entity.Food;
import com.lazysyntax.nutron.nutrition.repository.FoodRepository;
import com.lazysyntax.nutron.nutrition.converter.FoodConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


import static com.lazysyntax.nutron.nutrition.converter.FoodConverters.*;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Transactional
    public Food createFood(FoodDto foodDto) {
        Food foodEntity = toEntity(foodDto);
        return foodRepository.save(foodEntity);
    }

    @Transactional(readOnly = true)
    public List<FoodDto> getFoodsByUserId(String userId) {
        return foodRepository.findAllByUserId(userId).stream().map(FoodConverters::toDto).toList();
    }
}
