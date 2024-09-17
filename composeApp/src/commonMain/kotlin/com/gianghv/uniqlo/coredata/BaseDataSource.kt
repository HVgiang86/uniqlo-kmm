package com.gianghv.uniqlo.coredata

import coil3.network.HttpException
import com.gianghv.uniqlo.util.logging.AppLogger
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseDataSource : KoinComponent {
    private val ioDispatcher: CoroutineDispatcher by inject()

    protected fun getContext() = ioDispatcher

    protected suspend fun <R> result(
        block: suspend () -> BaseResponse<R>,
    ): Result<R> {
        try {
            val response = block.invoke()
            if (response.code in 200..299) {
                if (!response.isSuccessful()) {
                    return Result.Error(Exception(response.message))
                }

                val result = response.getSuccessfulData()
                println("[DEBUG] $result")

                return Result.Success(result)
            } else {
                return Result.Error(Exception(response.message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }

    protected suspend fun <R> resultT(
         block: suspend () -> String,
    ): Result<R> {
        var rawResponse: String? = null
        var statusCode: Int? = null
        var message: String? = null
        try {
            rawResponse = block.invoke()
            val r = Json.parseToJsonElement(rawResponse).jsonObject
            val messageJsonObj = r["message"]

            if (messageJsonObj != null) {
                message = messageJsonObj.jsonPrimitive.content
            }

            val statusCodeJsonObj = r["statusCode"]

            if (statusCodeJsonObj != null) {
                statusCode = statusCodeJsonObj.jsonPrimitive.int
            }

            val response: BaseResponse<R> = Json.decodeFromString(rawResponse)

            if (response.code in 200..299) {
                if (!response.isSuccessful()) {
                    return Result.Error(Exception(response.message))
                }

                val result = response.getSuccessfulData()
                println("[DEBUG] $result")

                return Result.Success(result)
            } else {
                return Result.Error(Exception(response.message))
            }
        } catch (e: JsonConvertException) {
            if (statusCode != null && message != null) {
                return Result.Error(Exception("[$statusCode] $message"))
            }
            return Result.Error(e)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }

    suspend fun HttpResponse.toError(): Throwable? {
        try {
            val error = Json.parseToJsonElement(this.bodyAsText()) as JsonObject
            error["message"]
            return Exception(error["message"].toString())
        } catch (e: JsonConvertException) {
            e.printStackTrace()
            return null
        }
    }

    protected suspend fun returnIfSuccess(
        block: suspend () -> BaseResponse<*>,
    ): Result<Boolean> {
        try {
            val response = block.invoke()
            if (response.code == 200) {
                val data = response.data
                val message = response.message
                println("[DEBUG] ${response.code} $message $data")

                if (data == null) return Result.Error(Exception(response.message))

                return Result.Success(true)
            } else {
                return Result.Error(Exception(response.message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }
}

fun BaseResponse<*>.toErrorEntity(): ErrorEntity {
    when (this.code) {
        in 400..499 -> {
            return ErrorEntity.ApiError(this.message, this.code)
        }

        in 500..599 -> {
            return ErrorEntity.ApiError(this.message, this.code)
        }

        else -> {
            AppLogger.e("Unknown error: [${this.code}]${this.message}")
            return ErrorEntity.unexpected(Exception("Unknown error"))
        }
    }
}

fun Exception.toErrorEntity(): ErrorEntity {
    when (this) {
        is HttpException -> {
            val code = this.response.code
            return ErrorEntity.apiError(this.message, code)
        }

        is kotlinx.io.IOException -> {
            return ErrorEntity.networkConnection()
        }

        else -> {
            return ErrorEntity.unexpected(this)
        }
    }
}
