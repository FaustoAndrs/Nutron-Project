package com.lazysyntax.nutron.nutrition.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealDto {
    private String id;
    private String userId;
    private String name;
    private String date;
    private List<MealFoodSnapshotDto> foods = new ArrayList<>();
}
