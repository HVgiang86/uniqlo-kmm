package com.gianghv.uniqlo.presentation.screen.login

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.User

@Immutable
sealed class LoginUiEvent : Reducer.UiEvent{
    data class Loading(val shouldShowPopup: Boolean) : LoginUiEvent()
    data class LoginSuccess(val user: User) : LoginUiEvent()
    data class LoginFail(val error: Throwable) : LoginUiEvent()
}
