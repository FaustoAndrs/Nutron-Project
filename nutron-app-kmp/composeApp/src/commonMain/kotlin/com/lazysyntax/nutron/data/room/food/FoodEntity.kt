package com.lazysyntax.nutron.data.room.food

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Entity(tableName = "foods")
@Serializable
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val foodId: Long = 0,
    val barcode: String?,
    val userId: String?,// ID que se comparará con el JWToken en MongoDB
    val name: String?,
    val lastUpdate: Long = Clock.System.now().toEpochMilliseconds(),
    val isSynced: Boolean = false,
    val nameEs: String?,
    val nameEn: String?,
    @Embedded val nutriments: NutrimentsEntity?,
    val nutriscoreGrade: String?,
    val brands: String?
)

@Serializable
data class NutrimentsEntity(
    val quantity: String? = "100",
    val quantityUnit: String? = "g",
    val calories: Double?,
    val proteins: Double?,
    val carbs: Double?,
    val fat: Double?,
    val saturatedFat: Double?,
    val sugars: Double?,
    val salt: Double?
)
