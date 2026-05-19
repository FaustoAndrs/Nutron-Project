package com.lazysyntax.nutron.nutrition.model.dto.MealRequest;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class FoodDto {
    private String foodId;
    private String barcode;
    private String userId;
    private String name;
    private String nameEs;
    private String nameEn;
    private NutrimentsDto nutriments;
    private String nutriscoreGrade;
    private String brands;

    public FoodDto() {
    }

    public FoodDto(String foodId, String barcode, String userId, String name, String nameEs, String nameEn, NutrimentsDto nutriments, String nutriscoreGrade, String brands) {
        this.foodId = foodId;
        this.barcode = barcode;
        this.userId = userId;
        this.name = name;
        this.nameEs = nameEs;
        this.nameEn = nameEn;
        this.nutriments = nutriments;
        this.nutriscoreGrade = nutriscoreGrade;
        this.brands = brands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodDto foodDto = (FoodDto) o;
        return Objects.equals(foodId, foodDto.foodId) && Objects.equals(barcode, foodDto.barcode) && Objects.equals(userId, foodDto.userId) && Objects.equals(name, foodDto.name) && Objects.equals(nameEs, foodDto.nameEs) && Objects.equals(nameEn, foodDto.nameEn) && Objects.equals(nutriments, foodDto.nutriments) && Objects.equals(nutriscoreGrade, foodDto.nutriscoreGrade) && Objects.equals(brands, foodDto.brands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, barcode, userId, name, nameEs, nameEn, nutriments, nutriscoreGrade, brands);
    }

    @Override
    public String toString() {
        return "FoodDto{" +
               "foodId='" + foodId + '\'' +
               ", barcode='" + barcode + '\'' +
               ", userId='" + userId + '\'' +
               ", name='" + name + '\'' +
               ", nameEs='" + nameEs + '\'' +
               ", nameEn='" + nameEn + '\'' +
               ", nutriments=" + nutriments +
               ", nutriscoreGrade='" + nutriscoreGrade + '\'' +
               ", brands='" + brands + '\'' +
               '}';
    }
}
