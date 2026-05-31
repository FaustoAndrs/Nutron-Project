package com.lazysyntax.nutron.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.lazysyntax.nutron.data.local.NutronDatabase
import com.lazysyntax.nutron.data.local.NutronDatabaseConstructor
import com.lazysyntax.nutron.data.local.getRoomDatabase
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSUserDefaults

@OptIn(ExperimentalForeignApi::class)
actual fun platformModule(): Module = module {

    single { getRoomDatabase(get()) }

    single<RoomDatabase.Builder<NutronDatabase>> {
        // Obtenemos la ruta a la carpeta Documents (más seguro que NSHomeDirectory)
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val dbFilePath = documentDirectory?.path + "/nutron.db"

        Room.databaseBuilder<NutronDatabase>(
            name = dbFilePath!!,
            factory = { NutronDatabaseConstructor.initialize() } // Requerido para Room KMP
        )
    }

    // el REsto de settings de tus settings
    single<Settings>(named("encrypted")) {
        KeychainSettings(service = "com.lazysyntax.nutron")
    }

    single<Settings>(named("common")) {
        NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
    }
}