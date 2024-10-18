package com.gianghv.uniqlo.data.source.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CartItemOrderRequest(
    val id: Long,
    val variationId: Long,
    val size: String,
    val quantity: Int
)
