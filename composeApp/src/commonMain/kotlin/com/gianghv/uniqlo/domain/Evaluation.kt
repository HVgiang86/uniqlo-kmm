package com.gianghv.uniqlo.domain

import kotlinx.serialization.Serializable

@Serializable
data class Evaluation(
    val id: Long,
    val star: Double,
    val content: String,
    val account: User
)
