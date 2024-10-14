package com.gianghv.uniqlo.domain

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: Long,
    val variationId: Long?,
    val size: String? = "M",
    val quantity: Int? = 0,
    val variation: CartItemVariation?,
)
