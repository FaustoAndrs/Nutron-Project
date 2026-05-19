package com.lazysyntax.nutron.nutrition.repository;

import com.lazysyntax.nutron.nutrition.model.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, String> {
    // Puedes añadir métodos de consulta personalizados aquí si los necesitas
}
