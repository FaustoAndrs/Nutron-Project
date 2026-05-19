package com.lazysyntax.nutron.data.remote.synchronization

import com.lazysyntax.nutron.presentation.ui.features.setUp.Activity
import com.lazysyntax.nutron.presentation.ui.features.setUp.Goal
import com.lazysyntax.nutron.presentation.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.presentation.ui.features.targets.TargetsUiState
import com.lazysyntax.nutron.domain.models.TargetDto
import com.lazysyntax.nutron.domain.models.UserSetup

fun SetUpUiState.toUserSetupEntity(): UserSetup {
    return UserSetup(
        weight = weight,
        height = height,
        age = age,
        gender = gender,
        activity = activity.name,
        goal = goal.name,
        formula = formula,
        diet = diet
    )
}

fun TargetsUiState.toTargetDto(): TargetDto {
    return TargetDto(
        diet = diet.name
    )
}


fun UserSetupResponse.toSetupUiState(): SetUpUiState {
    return SetUpUiState(
        weight = weight,
        height = height,
        age = age,
        gender = gender,
        activity = getActivity(activity),
        goal = getGoal(goal),
        formula = formula,
        diet = diet
    )
}

sealed class SyncResult {
    object Success : SyncResult()     // 200 OK
    object NotFound : SyncResult()    // 404 - Usuario nuevo sin settings
    object Error : SyncResult()       // Otros errores (Timeout, 500, etc)
}

fun getActivity(activity: String): Activity {
    return try {
        Activity.valueOf(activity)
    } catch (e: IllegalArgumentException) {
        println("ERROR: GET ACTIVITY [valor = low] $e")
        Activity.LOW
    }
}

fun getGoal(goal: String): Goal {
    return try {
        Goal.valueOf(goal)
    } catch (e: IllegalArgumentException) {
        println("ERROR: GET GOAL por defecto [valor = maintain] $e")
        Goal.MAINTAIN
    }
}
