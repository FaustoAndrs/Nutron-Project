package com.lazysyntax.nutron

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform