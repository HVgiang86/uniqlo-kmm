package com.gianghv.uniqlo.data.source.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val role: String = "user"
)
