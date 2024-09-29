package com.gianghv.uniqlo.domain

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long,
    val name: String? = "Unnamed Category",
    val active: Boolean? = false
) :  BaseModel()
