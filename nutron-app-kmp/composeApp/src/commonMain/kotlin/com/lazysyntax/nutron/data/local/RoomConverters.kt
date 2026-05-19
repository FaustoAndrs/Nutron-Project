package com.lazysyntax.nutron.data.local

import androidx.room.TypeConverter
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Recipe
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

class RoomConverters {
    private val json = Json { 
        ignoreUnknownKeys = true 
        encodeDefaults = true
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromFoodList(value: List<Food>): String = json.encodeToString(value)

    @TypeConverter
    fun toFoodList(value: String): List<Food> = json.decodeFromString(value)

    @TypeConverter
    fun fromRecipeList(value: List<Recipe>): String = json.encodeToString(value)

    @TypeConverter
    fun toRecipeList(value: String): List<Recipe> = json.decodeFromString(value)
}