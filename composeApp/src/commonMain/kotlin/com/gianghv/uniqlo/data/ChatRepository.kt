package com.gianghv.uniqlo.data

import com.gianghv.uniqlo.domain.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getAllMessages(userId: Long): Flow<List<ChatMessage>>
    suspend fun sendMessage(userId: Long, message: String): Flow<ChatMessage>
}
