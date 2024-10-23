package com.gianghv.uniqlo.domain

import kotlinx.serialization.Serializable

@Serializable
data class OrderHistory(
    val id: Long,
    val name: String? = "Untitled Order",
    val phone: String? = "",
    val address: String? = "",
    val note: String? = "",
    val total: String? = "0.0",
    val date: String? = null,
    val pay: Boolean? = false,
    val status: String? = "pending",
    val details: List<OrderDetail>? = emptyList(),
)

@Serializable
data class OrderDetail(
    val id: Long,
    val quantity: Long? = 0,
    val variation: OrderDetailVariation? = null,
)

@Serializable
data class OrderDetailVariation(
    val name: String? = "",
    val price: String? = "0.0",
    val color: String? = "",
    val image: String? = "",
    val productId: Long? = 0,
    val variationId: Long? = 0,
)
