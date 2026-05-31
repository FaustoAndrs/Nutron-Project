package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.data.local.meal.MealDao
import com.lazysyntax.nutron.data.local.meal.MealEntity
import com.lazysyntax.nutron.data.local.meal.MealFoodSnapshotEntity
import com.lazysyntax.nutron.data.local.meal.MealWithFoods
import com.lazysyntax.nutron.data.local.meal.toDomain
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.data.remote.food.FoodRemoteDataSource
import com.lazysyntax.nutron.data.remote.meal.MealRemoteDataSource
import com.lazysyntax.nutron.data.remote.meal.toDto
import com.lazysyntax.nutron.data.remote.meal.toEntity
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Meal
import com.lazysyntax.nutron.domain.repository.FoodRepository
import com.lazysyntax.nutron.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class MealRepositoryImpl(
    private val mealDao: MealDao,
    private val sessionManager: SessionManager,
    private val mealRemoteDataSource: MealRemoteDataSource,
) : MealRepository {

    override suspend fun createMeal(name: String, foods: List<Food>) {
        // 1. Inserta un nuevo registro Meal vinculado al Usuario
        val userId = sessionManager.getUserId() ?: throw Exception()
        mealDao.insertMeal(MealEntity(
            name = name,
            userId = userId,
            date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        ))
    }

    override suspend fun deleteMeal(mealId: String) {
        mealDao.deleteMealById(mealId)
    }

    override suspend fun getMealsByDate(date: LocalDate): List<Meal> {
        return mealDao.getMealsWithFoodsByDate(date).first().map { it.toDomain() }
    }

    override fun getMealsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Meal>> {
        return mealDao.getMealsWithFoodsByDateRange(startDate, endDate)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun insertMealWithFood(
        mealEntity: MealEntity,
        snapshots: List<MealFoodSnapshotEntity>
    ) {
        // 1. Insert en Local
        mealDao.insertMealWithSnapshots(mealEntity, snapshots)

        // 2. Insert en Remoto
        val mealWithFoods = MealWithFoods(mealEntity, snapshots)
        val isSynced = mealRemoteDataSource.saveMeal(mealWithFoods.toDto())

        // 3. Si se ha sincronizado con la base de datos remota, se actualiza el flag de sincronización
        if (isSynced) {
            mealDao.updateMeal(mealEntity.copy(isSynced = true))
        }
    }

    override suspend fun downloadAndSyncMeals(): Boolean {
        return try {
            val remoteMeals = mealRemoteDataSource.getMealsByUser()
            remoteMeals.forEach { dto ->
                // Convertimos DTO a Entidad y snapshots
                val entity = dto.toEntity(isSynced = true)
                val snapshots = dto.foods.map { it.toEntity(dto.id) }

                mealDao.insertMealWithSnapshots(entity, snapshots)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun syncPendingMeals(): Int {
        val pending = mealDao.getUnsyncedMeals().first()
        var count = 0
        pending.forEach { mealWithFoods ->
            val isSynced = mealRemoteDataSource.saveMeal(mealWithFoods.toDto())
            if (isSynced) {
                mealDao.updateMeal(mealWithFoods.meal.copy(isSynced = true))
                count++
            }
        }
        return count
    }

    override fun getUnsyncedMealsCount(): Flow<Int> {
        return mealDao.getUnsyncedMealsCount()
    }
}