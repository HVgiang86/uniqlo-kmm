package com.gianghv.uniqlo.presentation.screen.profile

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.User


@Immutable
data class ProfileUiState(
    override val isLoading: Boolean, override val error: ErrorState?, val user: User? = null, val isLogout: Boolean = false
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = ProfileUiState(
            isLoading = false, error = null
        )
    }
}
