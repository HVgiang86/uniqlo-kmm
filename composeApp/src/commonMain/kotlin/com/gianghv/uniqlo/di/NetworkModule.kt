package com.gianghv.uniqlo.di

import com.gianghv.uniqlo.constant.NETWORK_TIMEOUT
import com.gianghv.uniqlo.data.source.remote.api.CartApi
import com.gianghv.uniqlo.data.source.remote.api.ChatApi
import com.gianghv.uniqlo.data.source.remote.api.ProductApi
import com.gianghv.uniqlo.data.source.remote.api.UserApi
import com.gianghv.uniqlo.util.logging.AppLogger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                    coerceInputValues = true
                }, contentType = ContentType.Any)
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        AppLogger.i("Logger Ktor => $message")
                    }

                }
                level = LogLevel.ALL
            }
            install(ResponseObserver) {
                onResponse { response ->
                    AppLogger.d("HTTP status: ${response.status.value}")
                }
            }
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = NETWORK_TIMEOUT
                connectTimeoutMillis = NETWORK_TIMEOUT
                socketTimeoutMillis = NETWORK_TIMEOUT
            }
        }
    }
}

val apiServiceModule = module {
    factory { UserApi(get()) }
    factory { ProductApi(get()) }
    factory { CartApi(get()) }
    factory { ChatApi(get()) }
}

