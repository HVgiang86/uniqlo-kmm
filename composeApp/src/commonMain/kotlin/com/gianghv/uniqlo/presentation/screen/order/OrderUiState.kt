package com.gianghv.uniqlo.presentation.screen.order

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer

@Immutable
data class OrderUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,

) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = OrderUiState(
            isLoading = false, error = null
        )
    }
}
