package com.lazysyntax.nutron.nutrition.model.dto.MealRequest;

import java.util.Objects;

public class NutrimentsDto {
    private String quantity = "100";
    private String quantityUnit = "g";
    private Double calories;
    private Double proteins;
    private Double carbs;
    private Double fat;
    private Double saturatedFat;
    private Double sugars;
    private Double salt;

    public NutrimentsDto() {
    }

    public NutrimentsDto(String quantity, String quantityUnit, Double calories, Double proteins, Double carbs, Double fat, Double saturatedFat, Double sugars, Double salt) {
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.calories = calories;
        this.proteins = proteins;
        this.carbs = carbs;
        this.fat = fat;
        this.saturatedFat = saturatedFat;
        this.sugars = sugars;
        this.salt = salt;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getProteins() {
        return proteins;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(Double saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public Double getSugars() {
        return sugars;
    }

    public void setSugars(Double sugars) {
        this.sugars = sugars;
    }

    public Double getSalt() {
        return salt;
    }

    public void setSalt(Double salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NutrimentsDto that = (NutrimentsDto) o;
        return Objects.equals(quantity, that.quantity) && Objects.equals(quantityUnit, that.quantityUnit) && Objects.equals(calories, that.calories) && Objects.equals(proteins, that.proteins) && Objects.equals(carbs, that.carbs) && Objects.equals(fat, that.fat) && Objects.equals(saturatedFat, that.saturatedFat) && Objects.equals(sugars, that.sugars) && Objects.equals(salt, that.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, quantityUnit, calories, proteins, carbs, fat, saturatedFat, sugars, salt);
    }

    @Override
    public String toString() {
        return "NutrimentsDto{" +
               "quantity='" + quantity + '\'' +
               ", quantityUnit='" + quantityUnit + '\'' +
               ", calories=" + calories +
               ", proteins=" + proteins +
               ", carbs=" + carbs +
               ", fat=" + fat +
               ", saturatedFat=" + saturatedFat +
               ", sugars=" + sugars +
               ", salt=" + salt +
               '}';
    }
}
