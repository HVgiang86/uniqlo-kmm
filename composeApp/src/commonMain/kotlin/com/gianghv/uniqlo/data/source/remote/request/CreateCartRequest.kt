package com.gianghv.uniqlo.data.source.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCartRequest(
    @SerialName("variation_id") val variationId: Long,
    @SerialName("account_id") val userId: Long,
    val quantity: Int,
    val size: String
)
