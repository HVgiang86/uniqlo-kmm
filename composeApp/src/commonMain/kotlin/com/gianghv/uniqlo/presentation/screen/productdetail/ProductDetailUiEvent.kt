package com.gianghv.uniqlo.presentation.screen.productdetail

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.domain.ProductVariation
import com.gianghv.uniqlo.presentation.screen.home.HomeUiEvent
import com.gianghv.uniqlo.presentation.screen.productdetail.components.VariationSize

@Immutable
sealed class ProductDetailUiEvent : Reducer.UiEvent {
    data class LoadProduct(val productId: Long) : ProductDetailUiEvent()
    data class AddToCart(val quantity: Int, val size: VariationSize, val variation: ProductVariation) : ProductDetailUiEvent()
    data class AddToCartSuccess(val productId: Long) : ProductDetailUiEvent()
    data class Checkout(val productId: Long, val quantity: Int, val size: VariationSize, val variation: ProductVariation) : ProductDetailUiEvent()
    data class CheckoutSuccess(val productId: Long) : ProductDetailUiEvent()
    data class LoadRecommendedProduct(val productId: Long) : ProductDetailUiEvent()
    data class LoadRecommendedProductSuccess(val productList: List<Product>) : ProductDetailUiEvent()
    data class LoadProductSuccess(val product: Product) : ProductDetailUiEvent()
    data class LoadProductFailed(val error: Throwable) : ProductDetailUiEvent()
    data class SelectVariation(val variation: ProductVariation? = null, val size: VariationSize? = null) : ProductDetailUiEvent()
    data class Error(val error: Throwable) : ProductDetailUiEvent()
    data class SetFavorite(val product: Product, val isFavorite: Boolean) : ProductDetailUiEvent()
    data class SetFavoriteSuccess(val product: Product, val isFavorite: Boolean) : ProductDetailUiEvent()
}
