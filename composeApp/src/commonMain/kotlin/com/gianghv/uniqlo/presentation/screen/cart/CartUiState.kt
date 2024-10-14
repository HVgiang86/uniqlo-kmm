package com.gianghv.uniqlo.presentation.screen.cart

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.CartItem

@Immutable
data class CartUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val cartItems: List<CartItem> = emptyList(),
    val selectedItems: List<CartItem> = emptyList(),
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = CartUiState(
            isLoading = false, error = null,
        )
    }
}

