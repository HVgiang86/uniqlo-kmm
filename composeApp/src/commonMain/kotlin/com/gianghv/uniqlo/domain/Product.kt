package com.gianghv.uniqlo.domain

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long,
    val name: String? = "Uniqlo Product",
    val price: Double? = 0.0,
    val description: String? = "",
    val specifications: String? = "",
    val defaultImage: String? = null,
    val averageRating: Double? = null,
    val numberRating: Int? = 0,
    val discountPercentage: Int? = 0,
    val active: Boolean? = false,
    val createdAt: String? = null,
    val brand: Brand? = null,
    val images: List<Image>? = emptyList(),
    val variations: List<ProductVariation>? = emptyList(),
    val category: Category? = null
) : BaseModel()
