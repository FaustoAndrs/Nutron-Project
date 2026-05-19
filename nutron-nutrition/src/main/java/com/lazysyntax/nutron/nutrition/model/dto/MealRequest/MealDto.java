package com.lazysyntax.nutron.nutrition.model.dto.MealRequest;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public class MealDto {
    private String id;
    private String userId;
    private String name;
    private String date; // LocalDate as String "YYYY-MM-DD"
    private List<MealFoodSnapshotDto> foods = new ArrayList<>();

    public MealDto() {
    }

    public MealDto(String id, String userId, String name, String date, List<MealFoodSnapshotDto> foods) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.date = date;
        this.foods = foods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MealFoodSnapshotDto> getFoods() {
        return foods;
    }

    public void setFoods(List<MealFoodSnapshotDto> foods) {
        this.foods = foods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealDto mealDto = (MealDto) o;
        return Objects.equals(id, mealDto.id) && Objects.equals(userId, mealDto.userId) && Objects.equals(name, mealDto.name) && Objects.equals(date, mealDto.date) && Objects.equals(foods, mealDto.foods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, date, foods);
    }

    @Override
    public String toString() {
        return "MealDto{" +
               "id='" + id + '\'' +
               ", userId='" + userId + '\'' +
               ", name='" + name + '\'' +
               ", date='" + date + '\'' +
               ", foods=" + foods +
               '}';
    }
}
