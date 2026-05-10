package com.lazysyntax.nutron.data.room.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lazysyntax.nutron.data.room.food.FoodEntity
import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Entity(tableName = "recipes")
@Serializable
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String?,
    val name: String?,
    val isSynced: Boolean = false,
    val lastUpdated: Long = Clock.System.now().toEpochMilliseconds(),
    val ingredients: List<FoodEntity>?,
    val recipes: List<RecipeEntity>?
)
