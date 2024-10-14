package com.gianghv.uniqlo.data.source.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeWishlistRequest(
    @SerialName("accountId")
    val userId: Long,
    val productId: Long,
    val isFavorite: Boolean
)
