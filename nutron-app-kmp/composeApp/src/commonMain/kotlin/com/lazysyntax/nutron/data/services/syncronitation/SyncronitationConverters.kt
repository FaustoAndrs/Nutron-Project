package com.lazysyntax.nutron.data.services.syncronitation

import com.lazysyntax.nutron.main.ui.features.setUp.Activity
import com.lazysyntax.nutron.main.ui.features.setUp.Goal
import com.lazysyntax.nutron.main.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.main.ui.features.targets.TargetsUiState
import com.lazysyntax.nutron.models.TargetEntity
import com.lazysyntax.nutron.models.UserSetup

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

fun TargetsUiState.toTargetEntity(): TargetEntity {
    return TargetEntity(
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
