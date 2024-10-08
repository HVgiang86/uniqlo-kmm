package com.gianghv.uniqlo.presentation.screen.productdetail

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product

@Immutable
sealed class ProductDetailUiEvent : Reducer.UiEvent {
    data class LoadProduct(val productId: Long) : ProductDetailUiEvent()
    data object AddToCart : ProductDetailUiEvent()
    data object Checkout : ProductDetailUiEvent()
    data class LoadProductSuccess(val product: Product) : ProductDetailUiEvent()
    data class LoadProductFailed(val error: Throwable) : ProductDetailUiEvent()
    data class Error(val error: Throwable) : ProductDetailUiEvent()
}
