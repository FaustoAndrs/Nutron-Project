package com.lazysyntax.nutron.data.local.food

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(
    tableName = "foods",
    indices = [Index(value = ["barcode", "userId"], unique = true)]
)
@Serializable
data class FoodEntity(
    @PrimaryKey
    val foodId: String = Uuid.random().toString(),
    val barcode: String?,
    val userId: String?,
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
