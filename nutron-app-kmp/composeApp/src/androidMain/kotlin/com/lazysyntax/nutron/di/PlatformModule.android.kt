package com.lazysyntax.nutron.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.lazysyntax.nutron.data.room.NutronDatabase
import com.lazysyntax.nutron.data.room.getRoomDatabase
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { getRoomDatabase(get()) }
    single<RoomDatabase.Builder<NutronDatabase>> {
        val context: Context = get()
        val dbFile = context.getDatabasePath("nutron.db")
        Room.databaseBuilder<NutronDatabase>(
            context = context.applicationContext,
            name = dbFile.absolutePath
        )
    }
    
    // Settings Cifrado para Tokens
    single<Settings>(named("encrypted")) {
        val context: Context = get()
        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        val delegate = EncryptedSharedPreferences.create(
            context, "auth_prefs", masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        SharedPreferencesSettings(delegate)
    }

    // Settings Normal para Preferencias (más rápido)
    single<Settings>(named("common")) {
        val context: Context = get()
        val delegate = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        SharedPreferencesSettings(delegate)
    }
}
