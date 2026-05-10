package com.lazysyntax.nutron.data.room.meal

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.lazysyntax.nutron.data.room.food.FoodEntity

data class MealWithFoods(
    @Embedded val meal: MealEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "barcode",
        associateBy = Junction(MealFoodCrossRef::class, parentColumn = "mealId", entityColumn = "foodBarcode")
    )
    val foods: List<FoodEntity>
)
