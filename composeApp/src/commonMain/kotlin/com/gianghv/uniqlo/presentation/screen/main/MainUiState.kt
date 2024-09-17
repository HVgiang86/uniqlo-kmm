package com.gianghv.uniqlo.presentation.screen.main

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.MainState
import com.gianghv.uniqlo.base.Reducer

@Immutable
class MainUiState(
    override val isLoading: Boolean, override val error: ErrorState?, val data: String
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = MainState(
            isLoading = true, error = null, data = "Haha Init"
        )
    }
}

