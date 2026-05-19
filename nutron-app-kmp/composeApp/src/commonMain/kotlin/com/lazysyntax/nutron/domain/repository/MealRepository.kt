package com.lazysyntax.nutron.domain.repository

import com.lazysyntax.nutron.data.local.meal.MealEntity
import com.lazysyntax.nutron.data.local.meal.MealFoodSnapshotEntity
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Meal
import kotlinx.datetime.LocalDate

interface MealRepository {
    suspend fun createMeal(name: String, foods: List<Food>)

    suspend fun deleteMeal(mealId: String)

    suspend fun getMealsByDate(date: LocalDate): List<Meal>

    fun getMealsByDateRange(startDate: LocalDate, endDate: LocalDate): kotlinx.coroutines.flow.Flow<List<Meal>>

    suspend fun insertMealWithFood(mealEntity: MealEntity, snapshots: List<MealFoodSnapshotEntity>)
}