package com.gianghv.uniqlo.presentation.screen.wishlist

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product

@Immutable
data class WishListUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val favoriteProductList: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList(),
    val cartCount: Int = 0
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = WishListUiState(
            isLoading = false, error = null
        )
    }
}
