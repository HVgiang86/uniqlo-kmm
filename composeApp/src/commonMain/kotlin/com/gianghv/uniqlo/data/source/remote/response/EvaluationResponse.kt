package com.gianghv.uniqlo.data.source.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class EvaluationResponse(
    val id: Long,
    val star: Double = 0.0,
    val account: AccountResponse?,
    val review: ReviewResponse?
)

@Serializable
data class AccountResponse(
    val id: Long,
    val name: String? = "",
    val imagePath: String? = ""
)

@Serializable
data class ReviewResponse(
    val id: Long,
    val content: String? = "",
    val createdAt: String? = ""
)
