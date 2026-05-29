package com.lazysyntax.nutron.nutrition.repository;

import com.lazysyntax.nutron.nutrition.model.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
    List<Food> findAllByUserId(String userId);
}
