package com.lazysyntax.nutron.nutrition.repository;

import com.lazysyntax.nutron.nutrition.model.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
    // Puedes añadir métodos de consulta personalizados aquí si los necesitas
}
