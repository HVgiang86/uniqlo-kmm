package com.gianghv.uniqlo.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: Long,
    val content: String,
    @SerialName("created_at")
    val createAt: String,
    @SerialName("is_user")
    val isUser: Boolean,
    @SerialName("session_number")
    val sessionNumber: Int
)
