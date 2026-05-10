package com.lazysyntax.nutron

import android.app.Application
import com.lazysyntax.nutron.di.initKoin
import org.koin.android.ext.koin.androidContext

class NutronApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@NutronApp)
        }
    }
}
