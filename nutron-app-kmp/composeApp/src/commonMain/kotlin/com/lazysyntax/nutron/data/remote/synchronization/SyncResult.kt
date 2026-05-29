package com.lazysyntax.nutron.data.remote.synchronization

sealed class SyncResult {
    object Success : SyncResult()     // 200 OK
    object NotFound : SyncResult()    // 404 - Usuario nuevo sin settings
    object NetworkError : SyncResult() // Error de conexión
    object Error : SyncResult()       // Otros errores (Timeout, 500, etc)
}