package com.lazysyntax.nutron.data.room

import androidx.room.TypeConverter
import com.lazysyntax.nutron.data.room.food.FoodEntity
import com.lazysyntax.nutron.data.room.recipe.RecipeEntity
import kotlinx.serialization.json.Json

// Ejemplo de convertidor para Room
class RoomConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromFoodList(value: List<FoodEntity>?): String? = json.encodeToString(value)

    @TypeConverter
    fun toFoodList(value: String?): List<FoodEntity>? = value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun fromRecipeList(value: List<RecipeEntity>?): String? = json.encodeToString(value)

    @TypeConverter
    fun toRecipeList(value: String?): List<RecipeEntity>? = value?.let { json.decodeFromString(it) }
}