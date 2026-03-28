package com.lazysyntax.nutron

import android.app.Application
import com.lazysyntax.nutron.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NutronApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NutronApp)
            modules(appModule)
        }
    }
}
