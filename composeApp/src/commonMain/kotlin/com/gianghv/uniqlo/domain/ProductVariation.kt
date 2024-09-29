package com.gianghv.uniqlo.domain

import kotlinx.serialization.Serializable

@Serializable
data class ProductVariation(
    val id: Long,
    val color: String? = "Unknown Color",
    val price: Double? = 0.0,
    val stock: Int? = 0,
    val sold: Int? = 0,
    val image: String? = null
) :  BaseModel()
