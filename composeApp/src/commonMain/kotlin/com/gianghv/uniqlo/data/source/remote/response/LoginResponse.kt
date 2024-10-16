package com.gianghv.uniqlo.data.source.remote.response

import com.gianghv.uniqlo.domain.BaseData
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val email: String,
    val id: Long,
    val name: String,
    val phone: String?,
    val imagePath: String?,
    val gender: String = "other",
    val role: String = "user",
    val active: Boolean = true,
    val birthday: String?,
    val accessToken: String?,
    val refreshToken: String?,
    val wishlists: List<Long>?
) : BaseData()
