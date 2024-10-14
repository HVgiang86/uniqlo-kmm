package com.gianghv.uniqlo.data.source.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCartResponse(
    val id: Long, @SerialName("variation_id") val variationId: Long, @SerialName("account_id") val userId: Long, val quantity: Int, val size: String
)
