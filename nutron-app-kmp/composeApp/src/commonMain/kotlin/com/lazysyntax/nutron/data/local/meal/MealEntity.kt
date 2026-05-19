package com.lazysyntax.nutron.data.local.meal

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Entity(tableName = "meals")
@Serializable
data class MealEntity(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val userId: String?,
    val name: String,
    val date: LocalDate,
    val lastUpdate: Long = Clock.System.now().toEpochMilliseconds(),
    val isSynced: Boolean = false,
)
