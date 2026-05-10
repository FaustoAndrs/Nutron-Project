package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.main.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.main.ui.features.targets.TargetsUiState
import com.lazysyntax.nutron.models.TargetEntity

interface UserRepository {
    suspend fun getUserSetups(id: String): Boolean
    suspend fun updateUserSetup(setUpUiState: SetUpUiState): Boolean
    suspend fun updateUserDiet(targetsUiState: TargetsUiState): Boolean
}