package com.lazysyntax.nutron.data.local

import androidx.room.RoomDatabase.Builder
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

//A través del platformModule cada plataforma define el DataBaseBuilder mediante Koin
fun getRoomDatabase(
    builder: Builder<NutronDatabase>
): NutronDatabase {
    return builder
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
