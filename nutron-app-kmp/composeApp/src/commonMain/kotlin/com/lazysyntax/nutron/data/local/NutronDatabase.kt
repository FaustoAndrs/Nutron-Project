package com.lazysyntax.nutron.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.lazysyntax.nutron.data.local.food.FoodDao
import com.lazysyntax.nutron.data.local.food.FoodEntity
import com.lazysyntax.nutron.data.local.meal.MealDao
import com.lazysyntax.nutron.data.local.meal.MealEntity
import com.lazysyntax.nutron.data.local.meal.MealFoodSnapshotEntity
import com.lazysyntax.nutron.data.local.recipe.RecipeEntity

@Database(
    entities = [
        MealEntity::class,
        MealFoodSnapshotEntity::class,
        FoodEntity::class,
        RecipeEntity::class],
    version = 4
)
@TypeConverters(RoomConverters::class)
@ConstructedBy(NutronDatabaseConstructor::class)
abstract class NutronDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun mealDao(): MealDao
    //abstract fun recipeDao(): RecipeDao
}
    // The Room compiler generates the implementation for this class.
    @Suppress("KotlinNoActualForExpect")
    expect object NutronDatabaseConstructor : RoomDatabaseConstructor<NutronDatabase> {
        override fun initialize(): NutronDatabase
    }
