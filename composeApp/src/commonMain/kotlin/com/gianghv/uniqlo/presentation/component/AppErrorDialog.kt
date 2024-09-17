package com.gianghv.uniqlo.presentation.component

import androidx.compose.runtime.Composable
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.network.sockets.SocketTimeoutException
import io.ktor.serialization.JsonConvertException

@Composable
fun AppErrorDialog(throwable: Throwable?, onDismissRequest: () -> Unit) {
    var message = ""
    message = when(throwable) {
        is JsonConvertException -> "JSON convert fail!"
        is HttpRequestTimeoutException -> "Connection time out!"
        is SocketTimeoutException -> "Connection time out!"
        is io.ktor.client.network.sockets.SocketTimeoutException -> "Connection time out!"
        is ServerResponseException -> "Server response fail!"
        is ClientRequestException -> "Client request fail!"
        is Exception -> throwable.message ?: "Unknown error!"
        else -> "Unknown error!"
    }

    MyAlertDialog(title = "Thông báo", content = message, rightBtnTitle = "OK", rightBtn = {
        onDismissRequest()
    })
}
