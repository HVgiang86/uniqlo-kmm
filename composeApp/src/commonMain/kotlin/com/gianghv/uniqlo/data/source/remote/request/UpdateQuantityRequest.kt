package com.gianghv.uniqlo.data.source.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateQuantityRequest(
    val id: Long, val quantity: Int
)
