package com.gianghv.uniqlo.data.source.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderStatusRequest(
    val id: Long,
    val status: String
)
