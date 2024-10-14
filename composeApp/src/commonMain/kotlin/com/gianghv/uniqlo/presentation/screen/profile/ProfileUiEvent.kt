package com.gianghv.uniqlo.presentation.screen.profile

import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.domain.User

sealed class ProfileUiEvent : Reducer.UiEvent {
    data class Error(val error: Throwable) : ProfileUiEvent()
    data object LoadUser : ProfileUiEvent()
    data class LoadUserSuccess(val user: User) : ProfileUiEvent()
    data object Logout : ProfileUiEvent()
    data object LogoutSuccess : ProfileUiEvent()
}
