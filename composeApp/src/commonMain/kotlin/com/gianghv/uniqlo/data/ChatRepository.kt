package com.gianghv.uniqlo.data

import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun login()
    suspend fun receiveMessage(): Flow<String>
    suspend fun sendMessage(msg: String)
}
