package com.gianghv.uniqlo.data.source.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class UpdateQuantityResponse(
    val id: Long, val variationId: Long, val quantity: Int, val size: String
)
