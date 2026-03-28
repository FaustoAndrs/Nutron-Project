package com.lazysyntax.nutron

import androidx.compose.ui.window.ComposeUIViewController
import com.lazysyntax.nutron.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
