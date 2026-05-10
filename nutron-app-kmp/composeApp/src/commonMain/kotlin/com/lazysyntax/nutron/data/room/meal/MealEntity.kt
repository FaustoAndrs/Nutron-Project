package com.lazysyntax.nutron.data.room.meal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lazysyntax.nutron.data.room.food.FoodEntity
import com.lazysyntax.nutron.data.room.recipe.RecipeEntity
import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Entity(tableName = "meals")
@Serializable
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String?,
    val name: String?,
    val lastUpdate: Long = Clock.System.now().toEpochMilliseconds(),
    val isSynced: Boolean = false,
    val foods: List<FoodEntity>,
    val recipes: List<RecipeEntity>
)
