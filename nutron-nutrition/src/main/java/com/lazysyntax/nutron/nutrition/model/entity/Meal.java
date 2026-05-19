package com.lazysyntax.nutron.nutrition.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "meals")
public class Meal {
    @Id
    private String id;
    private String userId;
    private String name;
    private LocalDate date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "meal_id") // This creates a foreign key column in the meal_food_snapshots table
    private List<MealFoodSnapshot> foods = new ArrayList<>();

    public Meal() {
    }

    public Meal(String id, String userId, String name, LocalDate date, List<MealFoodSnapshot> foods) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<MealFoodSnapshot> getFoods() {
        return foods;
    }

    public void setFoods(List<MealFoodSnapshot> foods) {
        this.foods = foods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Objects.equals(id, meal.id) && Objects.equals(userId, meal.userId) && Objects.equals(name, meal.name) && Objects.equals(date, meal.date) && Objects.equals(foods, meal.foods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, date, foods);
    }

    @Override
    public String toString() {
        return "Meal{" +
               "id='" + id + '\'' +
               ", userId='" + userId + '\'' +
               ", name='" + name + '\'' +
               ", date=" + date +
               ", foods=" + foods +
               '}';
    }
}
