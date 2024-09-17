package com.gianghv.uniqlo.presentation.screen.login

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : BaseViewModel<LoginUiState, LoginUiEvent>() {
    override val reducer = LoginReducer(LoginUiState.initial())

    override val state: StateFlow<LoginUiState>
        get() = reducer.state

    override fun onClearLoadingState() {
    }

    override fun onClearErrorState() {
    }

    override val onException: ((Throwable) -> Unit) = {
        reducer.sendEvent(LoginUiEvent.LoginFail(it))
    }

    fun login(email: String, password: String) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            reducer.sendEvent(LoginUiEvent.Loading(true))

            delay(2000)
            if (email.isEmpty() || password.isEmpty()) {
                reducer.sendEvent(LoginUiEvent.LoginFail(Throwable("Email or password is empty")))
            } else {
                userRepository.login(email, password).collect {
                    reducer.sendEvent(LoginUiEvent.LoginSuccess(it))
                }
            }
        }
    }
}

class LoginReducer(initialVal: LoginUiState) : Reducer<LoginUiState, LoginUiEvent>(initialVal) {

    override fun reduce(oldState: LoginUiState, event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.LoginSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, loginSuccess = true))
            }

            is LoginUiEvent.Loading -> {
                setState(oldState.copy(isLoading = true, error = null, loginSuccess = false))
            }

            is LoginUiEvent.LoginFail -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true), loginSuccess = false))
            }
        }
    }
}
