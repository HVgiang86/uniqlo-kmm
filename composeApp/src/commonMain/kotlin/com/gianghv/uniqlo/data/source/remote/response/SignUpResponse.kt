package com.gianghv.uniqlo.data.source.remote.response

import com.gianghv.uniqlo.domain.BaseData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val email: String,
    val id: Long,
    val name: String,
    val phone: String?,
    @SerialName("image_path") val imagePath: String?,
    val gender: String = "other",
    val role: String = "user",
    val active: Boolean = true,
    @SerialName("password_reset") val passwordReset: String?,
    val birthday: String?
) : BaseData()

