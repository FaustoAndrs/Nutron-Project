package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.data.local.food.FoodDao
import com.lazysyntax.nutron.data.local.food.toDomain
import com.lazysyntax.nutron.data.local.food.toEntity
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.data.remote.food.FoodRemoteDataSource
import com.lazysyntax.nutron.data.remote.food.toDto
import com.lazysyntax.nutron.data.remote.food.toEntity
import com.lazysyntax.nutron.data.remote.meal.toDto
import com.lazysyntax.nutron.data.remote.meal.toEntity
import com.lazysyntax.nutron.data.remote.openFoodFactsApi.OpenFoodFactService
import com.lazysyntax.nutron.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
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
        val userId = sessionManager.getUserId() ?: throw Exception("User not logged in")

        // 1. Buscar si ya existe el alimento (por barcode) para mantener el mismo foodId
        val existingFood = if (!food.barcode.isNullOrBlank()) {
            foodDao.getFoodByBarcode(food.barcode)
        } else null

        val entity = food.toEntity(
            userId = userId,
            isSynced = false
        ).let { 
            if (existingFood != null) it.copy(foodId = existingFood.foodId) else it
        }
        
        // 2. Guardar localmente
        foodDao.insertFood(entity)
        
        // 3. Intentar guardar remotamente
        try {
            val isSynced = foodRemoteDataSource.saveFood(entity.toDto())
            if (isSynced) {
                foodDao.insertFood(entity.copy(isSynced = true))
            }
        } catch (e: Exception) {
            println("Error syncing food: ${e.message}")
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

    override suspend fun downloadAndSyncFoods(): Boolean {
        return try {
            val remoteFood = foodRemoteDataSource.getFoodsByUser()
            remoteFood.forEach { dto ->
                // Comprobamos si ya existe localmente para no crear duplicados con IDs distintos
                val existingLocal = if (!dto.barcode.isNullOrBlank()) {
                    foodDao.getFoodByBarcode(dto.barcode)
                } else null

                val entity = dto.toEntity().copy(
                    isSynced = true,
                    foodId = existingLocal?.foodId ?: dto.foodId ?: kotlin.uuid.Uuid.random().toString()
                )
                foodDao.insertFood(entity)
            }
            true
        } catch (e: Exception) {
            println("Error downloading foods: ${e.message}")
            false
        }
    }

    override suspend fun syncPendingFoods(): Int? {
        val userId = sessionManager.getUserId()
        var count = 0

        if(userId != null) {
            val pending =
                foodDao.getUnsyncedFoods(userId).first() // Necesitas añadir este query al DAO

            pending.forEach { food ->
                val isSynced = foodRemoteDataSource.saveFood(food.toDto())
                if (isSynced) {
                    foodDao.updateFood(
                        food
                    )
                    count++
                }
            }
            return count
        }else {
           return null
        }

    }
    override fun getUnsyncedFoodsCount(): Flow<Int>? {
        val userId = sessionManager.getUserId()
        return if(userId != null)
            foodDao.getUnsyncedFoodsCount(userId)
        else
            null
    }
}
