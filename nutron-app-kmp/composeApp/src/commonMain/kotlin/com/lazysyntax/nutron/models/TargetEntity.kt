package com.lazysyntax.nutron.models

import kotlinx.serialization.Serializable

@Serializable
data class TargetEntity(
    val diet:String = ""
    )