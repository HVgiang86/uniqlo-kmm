package com.gianghv.uniqlo.data.source.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimilarityProductResponse(
    @SerialName("id") val productId: Long, @SerialName("name") val productName: String
)
