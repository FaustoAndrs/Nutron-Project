package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.data.remote.NetworkConstants.AUTH_BASE_URL
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.data.remote.synchronization.SyncResult
import com.lazysyntax.nutron.data.remote.synchronization.UserSetupResponse
import com.lazysyntax.nutron.data.remote.synchronization.toSetupUiState
import com.lazysyntax.nutron.domain.repository.FoodRepository
import com.lazysyntax.nutron.domain.repository.MealRepository
import com.lazysyntax.nutron.domain.repository.SyncRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.time.Clock

class SyncRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager,
    private val mealRepository: MealRepository,
    private val foodRepository: FoodRepository
) : SyncRepository {
    private val BASE_URL = "${AUTH_BASE_URL}/user"
    private val SYNC_THRESHOLD = 86400000L // 24 horas

    override suspend fun syncUserSetUp(): SyncResult {
        return try {
            val response = client.get("$BASE_URL/setup") {
                contentType(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val settings = response.body<UserSetupResponse>()
                    val now = Clock.System.now().toEpochMilliseconds()
                    val uiState = settings.toSetupUiState()
                    println("SYNC: OK -> Datos recibidos: height=${uiState.height}, weight=${uiState.weight}")
                    sessionManager.saveUserProfile(uiState, lastSyncTime = now)

                    val userId = sessionManager.getUserId()
                    if (userId != null) {
                        mealRepository.downloadAndSyncMeals()
                        foodRepository.downloadAndSyncFoods()
                    }

                    SyncResult.Success
                }

                HttpStatusCode.NotFound -> {
                    println("SYNC: 404 Not Found")
                    SyncResult.NotFound
                }

                else -> {
                    println("SYNC: Error status ${response.status}")
                    SyncResult.Error
                }
            }
        } catch (e: Exception) {
            println("Error al sincronizar: ${e.message}")
            if (e.message?.contains("Failed to connect") == true || e.message?.contains("ConnectException") == true) {
                SyncResult.NetworkError
            } else {
                SyncResult.Error
            }
        }
    }

    override suspend fun syncUserSetUpIfNeeded(force: Boolean): SyncResult {

        val setupState = sessionManager.getCurrentUserData()

        // Comprobamos si hay datos reales
        val hasData = setupState.height.isNotEmpty() && setupState.weight.isNotEmpty()

        println("SYNC: DEBUG -> hasData=$hasData, height='${setupState.height}', weight='${setupState.weight}'")

        val now = Clock.System.now().toEpochMilliseconds()
        val lastSync = sessionManager.getUserPreferences().lastSyncTime
        val isStale = (now - lastSync) > SYNC_THRESHOLD

        println("SYNC: DEBUG -> force=$force, isStale=$isStale, now=$now, lastSync=$lastSync")

        return if (!force && hasData && !isStale) {
            println("SYNC: Resultado -> Usando local")
            SyncResult.Success
        } else {
            println("SYNC: Resultado -> Yendo a red")
            syncUserSetUp()
        }
    }
}
