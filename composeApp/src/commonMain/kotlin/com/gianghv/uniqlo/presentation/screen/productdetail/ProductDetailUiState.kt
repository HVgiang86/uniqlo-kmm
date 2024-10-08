package com.gianghv.uniqlo.presentation.screen.productdetail

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.domain.ProductVariation


@Immutable
data class ProductDetailUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val requireExitError: ErrorState? = null,
    val product: Product? = null,
    val selectedVariation: ProductVariation? = null,
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = ProductDetailUiState(
            isLoading = false, error = null,
        )
    }
}
