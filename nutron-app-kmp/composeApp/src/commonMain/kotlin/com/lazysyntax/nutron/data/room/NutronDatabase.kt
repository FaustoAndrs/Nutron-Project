package com.lazysyntax.nutron.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lazysyntax.nutron.data.room.food.FoodDao
import com.lazysyntax.nutron.data.room.food.FoodEntity
import com.lazysyntax.nutron.data.room.meal.MealDao
import com.lazysyntax.nutron.data.room.meal.MealEntity
import com.lazysyntax.nutron.data.room.meal.MealFoodCrossRef
import com.lazysyntax.nutron.data.room.recipe.RecipeEntity

@Database(
    entities = [
        FoodEntity::class,
        MealEntity::class,
        MealFoodCrossRef::class,
        RecipeEntity::class],
    version = 1
)
@TypeConverters(RoomConverters::class)
abstract class NutronDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun mealDao(): MealDao
    //abstract fun recipeDao(): RecipeDao
}
