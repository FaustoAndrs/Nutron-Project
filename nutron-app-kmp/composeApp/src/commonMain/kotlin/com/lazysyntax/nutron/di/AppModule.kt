package com.lazysyntax.nutron.di

import com.lazysyntax.nutron.data.services.authentication.AuthRepository
import com.lazysyntax.nutron.data.services.nutron.NutronService
import com.lazysyntax.nutron.data.services.nutron.OpenFoodFactsServiceImpl
import com.lazysyntax.nutron.main.ui.features.diary.DiaryViewModel
import com.lazysyntax.nutron.main.ui.features.login.LoginViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient {
            install(Logging) {
                //level = LogLevel.HEADERS // Similar to your HttpLoggingInterceptor
                level = LogLevel.ALL // To see headers and body debuggin
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Auth) {
                basic {
                    // This configures the client to send the basic auth header
                    // on every request to the specified host.
                    credentials {
                        BasicAuthCredentials(username = "off", password = "off")
                    }

                    // This ensures the header is sent proactively, without waiting for a 401 response.
                    sendWithoutRequest { request ->
                        // Configure this to only send credentials to your API's domain
                        request.url.host == "world.openfoodfacts.net"
                    }
                }
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS            }
            }
        }
    }
    single<NutronService> {
        OpenFoodFactsServiceImpl(get()) // Ktor's HttpClient is injected
    }
    single {
        OpenFoodFactsServiceImpl(get())
    }

    singleOf(::AuthRepository)
    viewModelOf(::LoginViewModel)
    viewModelOf(::DiaryViewModel)
}
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule)
    }
}
