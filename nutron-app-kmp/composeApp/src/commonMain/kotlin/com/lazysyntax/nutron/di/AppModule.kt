package com.lazysyntax.nutron.di

import com.lazysyntax.nutron.data.repository.FoodRepository
import com.lazysyntax.nutron.data.repository.FoodRepositoryImpl
import com.lazysyntax.nutron.data.repository.MealRepository
import com.lazysyntax.nutron.data.repository.MealRepositoryImpl
import com.lazysyntax.nutron.data.repository.UserRepository
import com.lazysyntax.nutron.data.repository.UserRepositoryImpl
import com.lazysyntax.nutron.data.room.NutronDatabase
import com.lazysyntax.nutron.data.services.authentication.AuthRepository
import com.lazysyntax.nutron.data.services.authentication.SessionManager
import com.lazysyntax.nutron.data.services.authentication.TokenResponse
import com.lazysyntax.nutron.data.services.openFoodFactsApi.OpenFoodFactService
import com.lazysyntax.nutron.data.services.openFoodFactsApi.OpenFoodFactsServiceImpl
import com.lazysyntax.nutron.data.services.syncronitation.SyncRepository
import com.lazysyntax.nutron.main.ui.features.diary.DiaryViewModel
import com.lazysyntax.nutron.main.ui.features.diary.library.LibraryViewModel
import com.lazysyntax.nutron.main.ui.features.diary.macros.MacrosViewModel
import com.lazysyntax.nutron.main.ui.features.login.LoginViewModel
import com.lazysyntax.nutron.main.ui.features.setUp.SetUpViewModel
import com.lazysyntax.nutron.main.ui.features.login.signUp.SignUpViewModel
import com.lazysyntax.nutron.main.ui.features.profile.ProfileViewModel
import com.lazysyntax.nutron.main.ui.features.settings.SettingsViewModel
import com.lazysyntax.nutron.main.ui.features.targets.TargetsViewModel
import com.lazysyntax.nutron.main.ui.navigation.DefaultNavigator
import com.lazysyntax.nutron.main.ui.navigation.Navigator
import com.lazysyntax.nutron.main.ui.navigation.Route
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {

    // Definimos SessionManager explícitamente para manejar los dos tipos de Settings
    single { 
        SessionManager(
            encryptedSettings = get(named("encrypted")),
            commonSettings = get(named("common"))
        ) 
    }

    single {
        val sessionManager: SessionManager = get()

        HttpClient {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP Client: $message")
                    }
                }
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = sessionManager.getAccessToken()
                        val refreshToken = sessionManager.getRefreshToken()

                        // DEBUG: Esto te dirá qué usuario cree la app que está activo justo antes de enviar
                        val userId = sessionManager.getUserId()
                        println("AUTH DEBUG: Cargando token para el usuario ID: $userId")

                        if (accessToken != null && refreshToken != null) {
                            BearerTokens(accessToken, refreshToken)
                        } else null
                    }
                    refreshTokens {
                        try {
                            // Usamos el cliente interno para la petición de refresh
                            val response = client.post("http://10.0.2.2:8081/api/v1/auth/refresh") {
                                contentType(ContentType.Application.Json)
                                setBody(oldTokens?.refreshToken)
                                markAsRefreshTokenRequest() 
                            }

                            when (response.status) {
                                HttpStatusCode.OK -> {
                                    val tokens = response.body<TokenResponse>()
                                    sessionManager.updateTokens(tokens.accessToken, tokens.refreshToken)
                                    BearerTokens(tokens.accessToken, tokens.refreshToken)
                                }
                                HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> {
                                    // SOLO aquí estamos seguros de que el Refresh Token no sirve
                                    sessionManager.logout()
                                    null
                                }
                                else -> {
                                    // Es un error del servidor (500) o algo temporal, no cerramos sesión
                                    null
                                }

                            }
                        } catch (e: Exception) {
                            // Es un error de red (no hay internet, timeout, etc.)
                            // NO llamamos a clearSession(), así el usuario mantiene su sesión
                            // para seguir usando la app offline.
                            null
                        }
                    }
                    sendWithoutRequest { request ->
                        // Solo enviamos tokens a nuestro backend. (Entorno local/laboratorio)
                        // En entorno de despliegue es importante establecer el dominio o Ip del backend.
                        request.url.host == "10.0.2.2" || request.url.host == "localhost"
                    }
                }
                basic {
                    credentials {
                        BasicAuthCredentials(username = "off", password = "off")
                    }
                    sendWithoutRequest { request ->
                        request.url.host == "world.openfoodfacts.net"
                    }
                }
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP // Cambiado a HTTP para desarrollo local si es necesario, o manten HTTPS
                }
            }
        }
    }
    //Navigation
    single<Navigator> {
        val sessionManager: SessionManager = get()
        val userData = sessionManager.getCurrentUserData()
        val isLoggedIn = sessionManager.isLoggedIn()
        
        println("NAV DEBUG: isLoggedIn=$isLoggedIn, height='${userData.height}'")

        val initialRoute = when {
            !isLoggedIn -> Route.Login
            else -> Route.Profile
        }
        println("NAV DEBUG: Ruta inicial decidida -> $initialRoute")
        DefaultNavigator(initialRoute = initialRoute)
    }

    // Services
    single<OpenFoodFactService> { OpenFoodFactsServiceImpl(get()) }

    // Database & DAOs
    single { get<NutronDatabase>().foodDao() }
    single { get<NutronDatabase>().mealDao() }

    // Repositories
    single<FoodRepository> { FoodRepositoryImpl(get(), get(), get()) }
    single<MealRepository> { MealRepositoryImpl(get(), get(),get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single { AuthRepository(get(), get()) }
    single { SyncRepository(get(), get()) }

    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SetUpViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::TargetsViewModel)
    viewModelOf(::DiaryViewModel)
    viewModelOf(::LibraryViewModel)
    viewModelOf(::MacrosViewModel)
    viewModelOf(::SettingsViewModel)
}

expect fun platformModule(): Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, platformModule())
    }
}
