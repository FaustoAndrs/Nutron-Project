package com.lazysyntax.nutron.data.remote.synchronization

// verfica que el token sea vigente y en caso de responder con un 200 OK
// Enviamos los elementos no sincronizados
// val pendientes = roomDb.mealDao().getUnsynced(currentUserId)
// roomDb.mealDao().markAsSynced(pendientes.map { it.id })

// cambiar de isUsserLogged a un estructura de setado de sesion usando un stateFlow que represente el estado de la sesion
//sealed class AuthState {
//    object Loading : AuthState()
//    object Authenticated : AuthState()
//    object Unauthenticated : AuthState()
//}