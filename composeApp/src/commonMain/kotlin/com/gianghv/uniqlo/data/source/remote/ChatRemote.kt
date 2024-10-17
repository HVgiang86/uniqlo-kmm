package com.gianghv.uniqlo.data.source.remote

import com.gianghv.uniqlo.coredata.BaseDataSource
import com.gianghv.uniqlo.coredata.Result
import com.gianghv.uniqlo.data.source.remote.api.ChatApi
import com.gianghv.uniqlo.domain.ChatMessage

interface ChatRemote {
    suspend fun getChatMessages(userId: Long): Result<List<ChatMessage>>
    suspend fun sendMessage(userId: Long, message: String): Result<ChatMessage>
}

class ChatRemoteImpl(private val api: ChatApi): ChatRemote, BaseDataSource() {
    override suspend fun getChatMessages(userId: Long): Result<List<ChatMessage>> = result {
        api.getChatMessages(userId)
    }

    override suspend fun sendMessage(userId: Long, message: String): Result<ChatMessage> = result {
        api.sendMessage(userId, message)
    }
}
