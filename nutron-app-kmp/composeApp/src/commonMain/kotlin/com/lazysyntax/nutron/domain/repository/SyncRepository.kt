package com.lazysyntax.nutron.domain.repository

import com.lazysyntax.nutron.data.remote.synchronization.SyncResult

interface SyncRepository {
    suspend fun syncUserSetUp(): SyncResult
    /**
     * Sincroniza solo si los datos están vacíos o si ha pasado mucho tiempo (Estrategia Offline-First)
     */
    suspend fun syncUserSetUpIfNeeded(force: Boolean = false): SyncResult
}