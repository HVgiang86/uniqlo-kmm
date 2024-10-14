package com.gianghv.uniqlo.data.source.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetWishlistResponse(
    val id: Long,
    @SerialName("account_id")
    val accountId: Long,
    @SerialName("product_id")
    val productId: Long,
)
