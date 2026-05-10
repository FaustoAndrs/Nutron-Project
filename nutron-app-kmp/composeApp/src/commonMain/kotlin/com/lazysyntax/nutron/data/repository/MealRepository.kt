package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.models.Food
import com.lazysyntax.nutron.models.Meal
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    suspend fun createMeal(name: String, foods: List<Food>)
    fun getAllMeals(): Flow<List<Pair<Meal, List<Food>>>>
    suspend fun deleteMeal(mealId: Long)
}
