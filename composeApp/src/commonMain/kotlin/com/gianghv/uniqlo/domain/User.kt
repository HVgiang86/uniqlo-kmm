package com.gianghv.uniqlo.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val id: Long,
    val name: String,
    val phone: String?,
    val imagePath: String?,
    val gender: String = "other",
    val role: String = "user",
    val active: Boolean = true,
    val passwordReset: String?,
    val birthday: String?,
    val wishList: List<Long>? = emptyList(),
): BaseModel()

