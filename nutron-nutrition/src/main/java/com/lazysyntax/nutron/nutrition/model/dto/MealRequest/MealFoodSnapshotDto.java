package com.lazysyntax.nutron.nutrition.model.dto.MealRequest;

import java.util.Objects;

public class MealFoodSnapshotDto {
    private String snapshotId;
    private String foodId;
    private String name;
    private String barcode;
    private NutrimentsDto nutriments;

    public MealFoodSnapshotDto() {
    }

    public MealFoodSnapshotDto(String snapshotId, String foodId, String name, String barcode, NutrimentsDto nutriments) {
        this.snapshotId = snapshotId;
        this.foodId = foodId;
        this.name = name;
        this.barcode = barcode;
        this.nutriments = nutriments;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public NutrimentsDto getNutriments() {
        return nutriments;
    }

    public void setNutriments(NutrimentsDto nutriments) {
        this.nutriments = nutriments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealFoodSnapshotDto that = (MealFoodSnapshotDto) o;
        return Objects.equals(snapshotId, that.snapshotId) && Objects.equals(foodId, that.foodId) && Objects.equals(name, that.name) && Objects.equals(barcode, that.barcode) && Objects.equals(nutriments, that.nutriments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snapshotId, foodId, name, barcode, nutriments);
    }

    @Override
    public String toString() {
        return "MealFoodSnapshotDto{" +
               "snapshotId='" + snapshotId + '\'' +
               ", foodId='" + foodId + '\'' +
               ", name='" + name + '\'' +
               ", barcode='" + barcode + '\'' +
               ", nutriments=" + nutriments +
               '}';
    }
}
