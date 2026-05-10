package com.lazysyntax.nutron.models

import com.lazysyntax.nutron.main.ui.features.setUp.Activity
import com.lazysyntax.nutron.main.ui.features.setUp.Goal
import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val userName: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val userSetup: UserSetup? = null
)

@Serializable
data class UserSetup(
    val weight: String = "",
    val height: String = "",
    val gender: String = "",
    val age: String = "",
    val fat: String = "",
    val activity: String = "",
    val goal: String = "",
    val formula: String = "",
    val diet: String = ""
)