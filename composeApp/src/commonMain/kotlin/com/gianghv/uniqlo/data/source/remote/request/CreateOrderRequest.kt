package com.gianghv.uniqlo.data.source.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    @SerialName("account_id")
    val userId: Long,
    val name: String,
    val address: String,
    val phone: String,
    val total: Long,
    val pay: Boolean,
    val note: String,
    @SerialName("products")
    val cartItems: List<CartItemOrderRequest>
)
