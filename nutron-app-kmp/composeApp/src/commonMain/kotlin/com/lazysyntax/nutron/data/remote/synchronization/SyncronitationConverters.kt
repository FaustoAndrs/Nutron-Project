package com.lazysyntax.nutron.data.remote.synchronization

import com.lazysyntax.nutron.presentation.ui.features.setUp.Activity
import com.lazysyntax.nutron.presentation.ui.features.setUp.Goal
import com.lazysyntax.nutron.presentation.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.presentation.ui.features.targets.TargetsUiState
import com.lazysyntax.nutron.domain.models.TargetDto
import com.lazysyntax.nutron.domain.models.UserSetup
import com.lazysyntax.nutron.presentation.ui.features.setUp.Gender

fun SetUpUiState.toUserSetupDto(): UserSetup {
    return UserSetup(
        weight = weight,
        height = height,
        age = age,
        gender = gender.name,
        activity = activity.name,
        goal = goal.name,
        formula = formula,
        diet = diet
    )
}

fun TargetsUiState.toTargetDto(): TargetDto = TargetDto(diet = diet.name)

fun UserSetupResponse.toSetupUiState(): SetUpUiState {
    return SetUpUiState(
        weight = weight,
        height = height,
        age = age,
        gender = Gender.valueOf(gender),
        activity = Activity.valueOf(activity),
        goal = Goal.valueOf(goal),
        formula = formula,
        diet = diet
    )
}