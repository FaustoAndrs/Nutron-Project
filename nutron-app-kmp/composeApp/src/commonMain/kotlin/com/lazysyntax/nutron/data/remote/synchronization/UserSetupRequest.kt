package com.lazysyntax.nutron.data.remote.synchronization

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

