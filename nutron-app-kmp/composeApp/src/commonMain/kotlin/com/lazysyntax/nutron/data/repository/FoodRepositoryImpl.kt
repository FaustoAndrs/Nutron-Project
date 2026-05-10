package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.data.room.food.FoodDao
import com.lazysyntax.nutron.data.room.food.toDomain
import com.lazysyntax.nutron.data.room.food.toEntity
import com.lazysyntax.nutron.data.services.authentication.SessionManager
import com.lazysyntax.nutron.models.Food
import com.lazysyntax.nutron.data.services.openFoodFactsApi.OpenFoodFactService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepositoryImpl(
    private val openFoodFactService: OpenFoodFactService,
    private val sessionManager: SessionManager,
    private val foodDao: FoodDao
) : FoodRepository {
    //API de OpenFoodFact
    override suspend fun fetchFoodByBarcode(barcode: String): Food? {
        return openFoodFactService.fetchFoodByBarcode(barcode)
    }

    override suspend fun searchFoodByName(name: String): List<Food> {
        return openFoodFactService.searchFoodByName(name)
    }

    //Operaciones con la base de datos local en Room
    override suspend fun saveFood(food: Food) {
        val userId = sessionManager.getUserId() ?: throw Exception()

        foodDao.insertFood(
            food.toEntity(
                userId = userId,
                isSynced = false
            )
        )
    }

    override fun getSavedFoods(): Flow<List<Food>> {
        return foodDao.getAllFoods().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getSavedFoodByCode(code: String): Food? {
        return foodDao.getFoodByCode(code)?.toDomain()
    }

    override suspend fun deleteFood(code: String) {
        foodDao.deleteFoodByCode(code)
    }
}
