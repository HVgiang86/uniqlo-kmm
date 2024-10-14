package com.gianghv.uniqlo.data.source.remote.response

import com.gianghv.uniqlo.domain.BaseData
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val email: String,
    val id: Long,
    val name: String,
    val phone: String?,
    val imagePath: String?,
    val gender: String = "other",
    val role: String = "user",
    val active: Boolean = true,
    val birthday: String?
): BaseData()
