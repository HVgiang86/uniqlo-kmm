package com.gianghv.uniqlo.presentation.screen.home

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product

@Immutable
data class HomeUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val productList: List<Product> = emptyList(),
    val recommendProducts: List<Product> = emptyList(),
    val cartCount : Int = 0
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = HomeUiState(
            isLoading = false, error = null,
        )
    }
}
