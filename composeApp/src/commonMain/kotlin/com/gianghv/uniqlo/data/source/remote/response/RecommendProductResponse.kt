package com.gianghv.uniqlo.data.source.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendProductResponse(
    @SerialName("product_id")
    val productId: Long,
)
