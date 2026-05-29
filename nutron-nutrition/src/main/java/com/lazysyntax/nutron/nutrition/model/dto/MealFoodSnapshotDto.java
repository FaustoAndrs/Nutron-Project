package com.lazysyntax.nutron.nutrition.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealFoodSnapshotDto {
    private String snapshotId;
    private String foodId;
    private String name;
    private String barcode;
    private NutrimentsDto nutriments;
}
