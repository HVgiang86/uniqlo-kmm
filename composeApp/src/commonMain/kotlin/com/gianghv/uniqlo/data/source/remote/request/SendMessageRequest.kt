package com.gianghv.uniqlo.data.source.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val userId: Long,
    val content: String
)
