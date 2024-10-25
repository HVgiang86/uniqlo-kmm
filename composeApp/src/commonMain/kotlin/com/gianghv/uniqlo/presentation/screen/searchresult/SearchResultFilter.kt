package com.gianghv.uniqlo.presentation.screen.searchresult

import com.gianghv.uniqlo.presentation.screen.productdetail.components.VariationSize

sealed class FilterType {
    class FilterSize(val size: VariationSize) : FilterType()
    class FilterColor(val color: String) : FilterType()
    class FilterPrice(val isDesc: Boolean = false) : FilterType()
    class FilterCategory(val category: String) : FilterType()
    class FilterBrand(val brand: String) : FilterType()
}

data class FilterState(
    val sizeFilter: FilterType.FilterSize? = null,
    val colorFilter: FilterType.FilterColor? = null,
    val priceFilter: FilterType.FilterPrice? = null,
    val categoryFilter: FilterType.FilterCategory? = null,
    val brandFilter: FilterType.FilterBrand? = null,
    val allColorList: List<String> = emptyList(),
    val allCategoryList: List<String> = emptyList(),
    val allBrandList: List<String> = emptyList(),
)
