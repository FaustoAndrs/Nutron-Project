package com.lazysyntax.nutron.data.local.recipe

import com.lazysyntax.nutron.domain.models.Recipe
import kotlinx.datetime.LocalDate


fun Recipe.toEntity(userId: String, date: LocalDate): RecipeEntity
{
    return RecipeEntity(
        name = name,
        ingredients = ingredients,
        userId = userId,

    )
}

fun RecipeEntity.toDomain(): Recipe {
return Recipe(
        name = name,
        ingredients = ingredients
    )
}