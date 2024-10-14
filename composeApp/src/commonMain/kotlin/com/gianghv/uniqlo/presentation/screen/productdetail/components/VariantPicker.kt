package com.gianghv.uniqlo.presentation.screen.productdetail.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import com.gianghv.uniqlo.domain.ProductVariation

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductSizeSelectSection(selected: VariationSize? = null, onSelect: (VariationSize) -> Unit) {
    FlowRow {
        val sizeList = listOf(VariationSize.XS, VariationSize.S, VariationSize.M, VariationSize.L, VariationSize.XL, VariationSize.XXL)

        sizeList.forEach { size ->
            VariationSizeButton(size = size, isSelected = size == selected, onSelected = {
                onSelect(it)
            })
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductColorSelectSection(variations: List<ProductVariation>, selected: ProductVariation? = null, onSelect: (ProductVariation) -> Unit) {

    FlowRow {
        variations.forEach { variation ->
            VariationColorButton(variation = variation, isSelected = variation == selected, onSelected = {
                onSelect(it)
            })
        }
    }
}
