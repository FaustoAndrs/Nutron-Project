package com.lazysyntax.nutron.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.lazysyntax.nutron.data.room.NutronDatabase
import com.lazysyntax.nutron.data.room.getRoomDatabase
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSUserDefaults

actual fun platformModule(): Module = module {

    single { getRoomDatabase(get()) }
    single<RoomDatabase.Builder<NutronDatabase>> {
        val dbFilePath = NSHomeDirectory() + "/nutron.db"
        Room.databaseBuilder<NutronDatabase>(
            name = dbFilePath,
            factory = { NutronDatabase::class as NutronDatabase }
        )
    }
    
    // Settings Cifrado para Tokens en iOS (Keychain)
    single<Settings>(named("encrypted")) { 
        KeychainSettings(service = "com.lazysyntax.nutron") 
    }

    // Settings Normal para Preferencias en iOS (NSUserDefaults)
    single<Settings>(named("common")) { 
        NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults) 
    }
}
