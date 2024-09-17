package com.gianghv.uniqlo.presentation.screen.auth

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer

@Immutable
data class AuthState(
    override val isLoading: Boolean, override val error: ErrorState?, val currentScreen: AuthCurrentScreen
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = AuthState(
            isLoading = false, error = null, currentScreen = AuthCurrentScreen.LOGIN
        )
    }
}

enum class AuthCurrentScreen {
    LOGIN, SIGNUP, MAIN
}
