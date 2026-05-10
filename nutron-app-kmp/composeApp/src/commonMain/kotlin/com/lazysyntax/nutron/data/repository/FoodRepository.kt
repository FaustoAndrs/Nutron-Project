package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.models.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    suspend fun fetchFoodByBarcode(barcode: String): Food?
    suspend fun searchFoodByName(name: String): List<Food>
    
    // Local operations
    suspend fun saveFood(food: Food)
    fun getSavedFoods(): Flow<List<Food>>
    suspend fun getSavedFoodByCode(code: String): Food?
    suspend fun deleteFood(code: String)
}
