package com.lazysyntax.nutron.nutrition.repository;

import com.lazysyntax.nutron.nutrition.model.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, String> {
    List<Meal> findAllByUserId(String userId);
}
