package com.lazysyntax.nutron.domain.repository

import com.lazysyntax.nutron.presentation.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.presentation.ui.features.targets.TargetsUiState

interface UserRepository {
    suspend fun getUserSetups(id: String): Boolean
    suspend fun updateUserSetup(setUpUiState: SetUpUiState): Boolean
    suspend fun updateUserDiet(targetsUiState: TargetsUiState): Boolean
}