package com.gianghv.uniqlo.data.source.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderResponse(
    val id: Long,
    val name: String? = "Untitled Order",
    val phone: String? = "",
    val address: String? = "",
    val note: String? = "",
    val total: String? = "0.0",
    val date: String? = null,
    val pay: Boolean? = false,
    val status: String? = "pending",
    @SerialName("account_id")
    val userId: Long? = 0,
)
