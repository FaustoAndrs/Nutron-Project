package com.lazysyntax.nutron.data.local.recipe

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lazysyntax.nutron.domain.models.Food
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "recipes")
@Serializable
data class RecipeEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val userId: String?,
    val name: String,
    val isSynced: Boolean = false,
    val lastUpdated: Long = Clock.System.now().toEpochMilliseconds(),
    val ingredients: List<Food>,
)
