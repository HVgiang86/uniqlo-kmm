package com.gianghv.uniqlo.data.source.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateEvaluationRequest(
    val productId: Long, val star: Double, val content: String, val accountId: Long
)
