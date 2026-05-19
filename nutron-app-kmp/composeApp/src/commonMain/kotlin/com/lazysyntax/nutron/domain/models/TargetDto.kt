package com.lazysyntax.nutron.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class TargetDto(
    val diet:String = ""
    )