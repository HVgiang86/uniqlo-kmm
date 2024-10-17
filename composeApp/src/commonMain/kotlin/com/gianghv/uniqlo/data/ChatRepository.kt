package com.gianghv.uniqlo.data

import com.gianghv.uniqlo.domain.ChatMessage
import dev.icerock.moko.socket.Socket
import dev.icerock.moko.socket.SocketEvent
import dev.icerock.moko.socket.SocketOptions
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getAllMessages(userId: Long): Flow<List<ChatMessage>>
    suspend fun sendMessage(userId: Long, message: String): Flow<ChatMessage>
}
