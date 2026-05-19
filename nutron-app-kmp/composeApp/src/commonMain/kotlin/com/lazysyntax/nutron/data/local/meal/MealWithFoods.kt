package com.lazysyntax.nutron.data.local.meal

import androidx.room.Embedded
import androidx.room.Relation

data class MealWithFoods(
    @Embedded val meal: MealEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "mealId"
    )
    val snapshots: List<MealFoodSnapshotEntity>
)
