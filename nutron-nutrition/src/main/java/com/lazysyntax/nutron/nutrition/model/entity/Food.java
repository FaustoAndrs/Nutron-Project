package com.lazysyntax.nutron.nutrition.model.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "foods")
public class Food {
    @Id
    private String foodId;
    private String barcode;
    private String userId; // Assuming a user can create custom foods
    private String name;
    private String nameEs;
    private String nameEn;

    @Embedded
    private Nutriments nutriments;

    private String nutriscoreGrade;
    private String brands;

    public Food() {
    }

    public Food(String foodId, String barcode, String userId, String name, String nameEs, String nameEn, Nutriments nutriments, String nutriscoreGrade, String brands) {
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

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEs() {
        return nameEs;
    }

    public void setNameEs(String nameEs) {
        this.nameEs = nameEs;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Nutriments getNutriments() {
        return nutriments;
    }

    public void setNutriments(Nutriments nutriments) {
        this.nutriments = nutriments;
    }

    public String getNutriscoreGrade() {
        return nutriscoreGrade;
    }

    public void setNutriscoreGrade(String nutriscoreGrade) {
        this.nutriscoreGrade = nutriscoreGrade;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Objects.equals(foodId, food.foodId) && Objects.equals(barcode, food.barcode) && Objects.equals(userId, food.userId) && Objects.equals(name, food.name) && Objects.equals(nameEs, food.nameEs) && Objects.equals(nameEn, food.nameEn) && Objects.equals(nutriments, food.nutriments) && Objects.equals(nutriscoreGrade, food.nutriscoreGrade) && Objects.equals(brands, food.brands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, barcode, userId, name, nameEs, nameEn, nutriments, nutriscoreGrade, brands);
    }

    @Override
    public String toString() {
        return "Food{" +
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
