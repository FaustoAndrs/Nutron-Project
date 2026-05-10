package com.lazysyntax.nutron.data.room.meal

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "meal_food_cross_ref",
    primaryKeys = ["mealId", "foodBarcode"],
    indices = [Index(value = ["foodBarcode"])]
)
data class MealFoodCrossRef(
    val mealId: Long,
    val foodBarcode: String
)
