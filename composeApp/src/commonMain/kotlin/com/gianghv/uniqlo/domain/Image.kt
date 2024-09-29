package com.gianghv.uniqlo.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val id: Long,
    @SerialName("image_path")
    val imagePath: String
) :  BaseModel()
