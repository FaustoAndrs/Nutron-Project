package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.data.room.food.toDomain
import com.lazysyntax.nutron.data.room.meal.MealDao
import com.lazysyntax.nutron.data.room.meal.MealEntity
import com.lazysyntax.nutron.data.room.meal.MealFoodCrossRef
import com.lazysyntax.nutron.data.services.authentication.SessionManager
import com.lazysyntax.nutron.models.Food
import com.lazysyntax.nutron.models.Meal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MealRepositoryImpl(
    private val mealDao: MealDao,
    private val sessionManager: SessionManager,
    private val foodRepository: FoodRepository
) : MealRepository {

    override suspend fun createMeal(name: String, foods: List<Food>) {
        // 1. Insert the meal and get its ID
        val userId = sessionManager.getUserId() ?: throw Exception()
        val mealId = mealDao.insertMeal(MealEntity(
            name = name,
            userId = userId,
            foods = TODO(),
            recipes = TODO()
        ))
        
        // 2. Insert each food (to ensure they exist in DB) and the relationship
        foods.forEach { food ->
            foodRepository.saveFood(food)
            mealDao.insertMealFoodCrossRef(
                MealFoodCrossRef(mealId = mealId, foodBarcode = food.barcode ?: "")
            )
        }
    }

    override fun getAllMeals(): Flow<List<Pair<Meal, List<Food>>>> {
        return mealDao.getAllMealsWithFoods().map { list ->
            list.map { mealWithFoods ->
                val meal = Meal(name = mealWithFoods.meal.name)
                val foods = mealWithFoods.foods.map { it.toDomain() }
                Pair(meal, foods)
            }
        }
    }

    override suspend fun deleteMeal(mealId: Long) {
        mealDao.deleteMealById(mealId)
    }
}
