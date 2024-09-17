package com.gianghv.uniqlo.presentation.screen.login

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer

@Immutable
data class LoginUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val loginSuccess: Boolean = false
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = LoginUiState(
            isLoading = false, error = null
        )
    }
}
