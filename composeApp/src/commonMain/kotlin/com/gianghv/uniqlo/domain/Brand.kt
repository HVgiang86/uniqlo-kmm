package com.gianghv.uniqlo.domain

import kotlinx.serialization.Serializable

@Serializable
data class Brand(
    val id : Long,
    val name : String? = "Unnamed Brand",
    val logo : String? = null,
    val active : Boolean? = false
) :  BaseModel()
