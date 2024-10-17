package com.gianghv.uniqlo.data.repository

import com.gianghv.uniqlo.coredata.BaseRepository
import com.gianghv.uniqlo.data.ChatRepository
import com.gianghv.uniqlo.data.source.remote.ChatRemote
import com.gianghv.uniqlo.domain.ChatMessage
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl(private val chatRemote: ChatRemote) : ChatRepository, BaseRepository() {

    override suspend fun getAllMessages(userId: Long): Flow<List<ChatMessage>> = flowContext {
        chatRemote.getChatMessages(userId)
    }

    override suspend fun sendMessage(userId: Long, message: String): Flow<ChatMessage> = flowContext {
        chatRemote.sendMessage(userId, message)
    }
}
