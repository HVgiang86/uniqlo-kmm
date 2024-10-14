package com.gianghv.uniqlo.presentation.screen.productdetail

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.domain.ProductVariation
import com.gianghv.uniqlo.presentation.screen.productdetail.components.VariationSize

@Immutable
data class ProductDetailUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val requireExitError: ErrorState? = null,
    val product: Product? = null,
    val recommendedProducts: List<Product> = emptyList(),
    val selectedVariation: ProductVariation? = null,
    val selectedSize: VariationSize? = null,
    val orderSuccess: Boolean = false,
    val addCartSuccess: Boolean = false,
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = ProductDetailUiState(
            isLoading = false, error = null,
        )
    }
}
