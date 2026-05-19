package com.lazysyntax.nutron.domain.repository

import com.lazysyntax.nutron.data.remote.synchronization.SyncResult

interface SyncRepository {
    suspend fun syncUserSetUp(): SyncResult
}