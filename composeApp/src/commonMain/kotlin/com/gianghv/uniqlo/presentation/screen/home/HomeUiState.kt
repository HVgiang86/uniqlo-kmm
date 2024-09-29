package com.gianghv.uniqlo.presentation.screen.home

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.presentation.screen.auth.AuthCurrentScreen

@Immutable
data class HomeUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val productList: List<Product> = emptyList(),
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = HomeUiState(
            isLoading = false, error = null,
        )
    }
}
