package com.gianghv.uniqlo.data.repository

import com.gianghv.uniqlo.data.ChatRepository
import com.gianghv.uniqlo.util.logging.AppLogger
import dev.icerock.moko.socket.Socket
import dev.icerock.moko.socket.SocketEvent
import dev.icerock.moko.socket.SocketOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ChatRepositoryImpl: ChatRepository {
    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow = _messageFlow.asSharedFlow()

    val socket = Socket(
        endpoint = "http://192.168.0.102:4646",
        config = SocketOptions(
            queryParams = mapOf("param1" to "1", "param2" to "2"),
            transport = SocketOptions.Transport.WEBSOCKET
        )
    ) {
        on(SocketEvent.Connect){
            println("Connected")
        }

        on(SocketEvent.Disconnect) {
            println("Disconnected")
        }

        on("response") { message ->
            _messageFlow.tryEmit(message)
        }
    }



    override suspend fun login() {
        AppLogger.d("Try to connect")
        if (!socket.isConnected())
            socket.connect()
    }

    override suspend fun receiveMessage(): Flow<String> {
        return messageFlow
    }

    override suspend fun sendMessage(msg: String) {
        socket.emit("message", msg)
    }
}
