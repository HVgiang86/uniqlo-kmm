package com.gianghv.uniqlo.presentation.screen.auth

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.User

@Immutable
sealed class AuthEvent : Reducer.UiEvent{
    data class Loading(val shouldShowPopup: Boolean) : AuthEvent()
    data object LoginSuccess: AuthEvent()
    data class LoginFail(val error: Throwable) : AuthEvent()
    data object SignUpSuccess : AuthEvent()
    data class SignUpFail(val error: Throwable) : AuthEvent()
    data object DisplaySignUp: AuthEvent()
    data object DisplayLogin: AuthEvent()
    data object NavigateMain: AuthEvent()
}
