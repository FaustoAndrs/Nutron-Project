package com.lazysyntax.nutron.data.local.meal

import androidx.room.*
import com.lazysyntax.nutron.data.local.recipe.RecipeEntity
import com.lazysyntax.nutron.domain.models.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Update
    suspend fun updateMeal(meal: MealEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodSnapshots(snapshots: List<MealFoodSnapshotEntity>)

    @Query("DELETE FROM meal_food_snapshots WHERE mealId = :mealId")
    suspend fun deleteFoodSnapshotsByMealId(mealId: String)

    @Transaction
    suspend fun insertMealWithSnapshots(meal: MealEntity, snapshots: List<MealFoodSnapshotEntity>) {
        insertMeal(meal)
        // Eliminamos los snapshots anteriores para evitar duplicados al actualizar la lista de alimentos
        deleteFoodSnapshotsByMealId(meal.id)
        val snapshotsWithId = snapshots.map { it.copy(mealId = meal.id) }
        insertFoodSnapshots(snapshotsWithId)
    }

    @Query("DELETE FROM meals WHERE id = :id")
    suspend fun deleteMealById(id: String)

    @Query("SELECT * FROM recipes WHERE lastUpdated > :lastSyncLocalTimestamp")
    fun getRecipesLastUpdated(lastSyncLocalTimestamp: Long): Flow<List<RecipeEntity>>

    @Transaction
    @Query("SELECT * FROM meals WHERE date = :date")
    fun getMealsWithFoodsByDate(date: LocalDate): Flow<List<MealWithFoods>>

    @Transaction
    @Query("SELECT * FROM meals WHERE date BETWEEN :startDate AND :endDate")
    fun getMealsWithFoodsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<MealWithFoods>>

    @Transaction
    @Query("SELECT * FROM meals WHERE isSynced = 0")
    fun getUnsyncedMeals(): Flow<List<MealWithFoods>>

    @Query("SELECT COUNT(*) FROM meals WHERE isSynced = 0")
    fun getUnsyncedMealsCount(): Flow<Int>

    @Transaction
    suspend fun clearAll() {
        deleteAllFoodSnapshots()
        deleteAllMeals()
        deleteAllRecipes()
    }

    @Query("DELETE FROM meal_food_snapshots")
    suspend fun deleteAllFoodSnapshots()

    @Query("DELETE FROM meals")
    suspend fun deleteAllMeals()

    @Query("DELETE FROM recipes")
    suspend fun deleteAllRecipes()
}
