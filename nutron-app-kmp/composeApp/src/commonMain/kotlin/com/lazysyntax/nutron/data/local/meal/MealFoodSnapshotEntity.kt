package com.lazysyntax.nutron.data.local.meal

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lazysyntax.nutron.data.local.food.NutrimentsEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "meal_food_snapshots",
    foreignKeys = [
        ForeignKey(
            entity = MealEntity::class,
            parentColumns = ["id"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("mealId")]
)
data class MealFoodSnapshotEntity(
    @PrimaryKey val snapshotId: String = Uuid.random().toString(),
    val mealId: String,
    val foodId: String?, // Referencia opcional al alimento original
    val name: String?,
    val barcode: String?,
    @Embedded val nutriments: NutrimentsEntity?
)
