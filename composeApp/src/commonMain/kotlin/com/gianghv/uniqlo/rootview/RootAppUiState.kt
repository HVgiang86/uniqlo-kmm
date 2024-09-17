package com.gianghv.uniqlo.rootview

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer

@Immutable
data class RootAppUiState(
    override val isLoading: Boolean,
    override val error: ErrorState?,
    val isLoggedIn: Boolean = false,
    val isFirstRun: Boolean = false
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = RootAppUiState(
            isLoading = false, error = null, isLoggedIn = false, isFirstRun = true
        )
    }
}
