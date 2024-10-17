package com.gianghv.uniqlo.data.source.remote.api

import com.gianghv.uniqlo.coredata.BaseResponse
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.data.source.remote.request.SendMessageRequest
import com.gianghv.uniqlo.domain.ChatMessage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ChatApi(private val httpClient: HttpClient) {
    suspend fun getChatMessages(userId: Long): BaseResponse<List<ChatMessage>> = httpClient.get("${WholeApp.CHAT_BASE_URL}/chat/$userId").body()
    suspend fun sendMessage(userId: Long, message: String): BaseResponse<ChatMessage> = httpClient.post("${WholeApp.CHAT_BASE_URL}/chat"){
        setBody(SendMessageRequest(userId, message))
    }.body()
}
