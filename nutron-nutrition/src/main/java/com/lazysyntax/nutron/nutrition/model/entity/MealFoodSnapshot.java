package com.lazysyntax.nutron.nutrition.model.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "meal_food_snapshots")
public class MealFoodSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String snapshotId;

    // Reference to the original Food entity, if it exists
    private String foodId;

    private String name;
    private String barcode;

    @Embedded
    private Nutriments nutriments;

    // ManyToOne relationship with Meal, but we'll handle the owning side in Meal for simplicity
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "meal_id")
    // private Meal meal;

    public MealFoodSnapshot() {
    }

    public MealFoodSnapshot(String snapshotId, String foodId, String name, String barcode, Nutriments nutriments) {
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

    public Nutriments getNutriments() {
        return nutriments;
    }

    public void setNutriments(Nutriments nutriments) {
        this.nutriments = nutriments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealFoodSnapshot that = (MealFoodSnapshot) o;
        return Objects.equals(snapshotId, that.snapshotId) && Objects.equals(foodId, that.foodId) && Objects.equals(name, that.name) && Objects.equals(barcode, that.barcode) && Objects.equals(nutriments, that.nutriments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snapshotId, foodId, name, barcode, nutriments);
    }

    @Override
    public String toString() {
        return "MealFoodSnapshot{" +
               "snapshotId='" + snapshotId + '\'' +
               ", foodId='" + foodId + '\'' +
               ", name='" + name + '\'' +
               ", barcode='" + barcode + '\'' +
               ", nutriments=" + nutriments +
               '}';
    }
}
