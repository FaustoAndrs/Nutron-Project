package com.lazysyntax.nutron.data.services.syncronitation

import com.lazysyntax.nutron.models.UserSetup
import kotlinx.serialization.Serializable

@Serializable
data class UserSetupRequest(
    val weight: String = "",
    val height: String = "",
    val age: String = "",
    val fat: String = "",
    val gender: String = "",
    val activity: String = "",
    val goal: String = "",
    val formula: String = ""

)

