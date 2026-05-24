package com.lazysyntax.nutron.data.local.meal

import com.lazysyntax.nutron.data.local.food.toDomain
import com.lazysyntax.nutron.data.local.food.toEntity
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Meal
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
fun Meal.toEntity(userId: String, date: LocalDate): MealEntity {
    return MealEntity(
        id = id ?: Uuid.random().toString(),
        name = name,
        userId = userId,
        date = date,
    )
}

/**
 * Crea la lista de snapshots a partir de los alimentos actuales del objeto Meal.
 */
fun Meal.toSnapshotEntities(mealId: String = ""): List<MealFoodSnapshotEntity> {
    return foods?.map { it.toSnapshotEntity(mealId) } ?: emptyList()
}

/**
 * Convierte un Food individual en un Snapshot (Desnormalización).
 */
fun Food.toSnapshotEntity(mealId: String): MealFoodSnapshotEntity {
    return MealFoodSnapshotEntity(
        mealId = mealId,
        foodId = id!!,
        name = name ?: nameEs ?: nameEn,
        barcode = barcode,
        nutriments = nutriments?.toEntity()
    )
}

/**
 * Mapea el POJO de relación de Room de vuelta al modelo de dominio.
 */
fun MealWithFoods.toDomain(): Meal {
    return Meal(
        id = meal.id,
        name = meal.name,
        foods = snapshots.map { it.toDomain() },
        recipes = emptyList(),
        date = meal.date
    )
}

/**
 * Convierte el snapshot almacenado de vuelta al modelo Food de la UI.
 */
fun MealFoodSnapshotEntity.toDomain(): Food {
    return Food(
        id = foodId,
        barcode = barcode,
        name = name,
        nutriments = nutriments?.toDomain()
    )
}
