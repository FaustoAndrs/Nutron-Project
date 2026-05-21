package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.data.local.food.FoodDao
import com.lazysyntax.nutron.data.local.food.toDomain
import com.lazysyntax.nutron.data.local.food.toEntity
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.data.remote.food.FoodRemoteDataSource
import com.lazysyntax.nutron.data.remote.food.toDto
import com.lazysyntax.nutron.data.remote.openFoodFactsApi.OpenFoodFactService
import com.lazysyntax.nutron.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepositoryImpl(
    private val openFoodFactService: OpenFoodFactService,
    private val sessionManager: SessionManager,
    private val foodDao: FoodDao,
    private val foodRemoteDataSource: FoodRemoteDataSource
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

        val entity = food.toEntity(
            userId = userId,
            isSynced = false
        )
        
        // 1. Save locally
        foodDao.insertFood(entity)
        
        // 2. Try to save remotely

        val isSynced = foodRemoteDataSource.saveFood(entity.toDto())
        
        // 3. If remote success, update local status
        if (isSynced) {
            foodDao.insertFood(entity.copy(isSynced = true))
        }
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

    override suspend fun searchSavedFoodByName(name: String): List<Food> {
        return foodDao.searchFoodByName(name).map { it.toDomain() }
    }
}
