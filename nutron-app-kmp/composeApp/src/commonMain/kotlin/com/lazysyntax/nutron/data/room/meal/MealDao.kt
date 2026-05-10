package com.lazysyntax.nutron.data.room.meal

import androidx.room.*
import com.lazysyntax.nutron.data.room.recipe.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealFoodCrossRef(crossRef: MealFoodCrossRef)

    @Transaction
    @Query("SELECT * FROM meals ORDER BY lastUpdate DESC")
    fun getAllMealsWithFoods(): Flow<List<MealWithFoods>>

    @Query("DELETE FROM meals WHERE id = :id")
    suspend fun deleteMealById(id: Long)

    @Query("SELECT * FROM recipes WHERE lastUpdated > :lastSyncLocalTimestamp")
    fun getRecipesLastUpdated(lastSyncLocalTimestamp: Long): Flow<List<RecipeEntity>>
}
