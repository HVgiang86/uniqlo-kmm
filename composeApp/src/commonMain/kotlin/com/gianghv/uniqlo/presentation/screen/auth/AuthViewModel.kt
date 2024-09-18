package com.gianghv.uniqlo.presentation.screen.auth

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.AppRepository
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.util.ValidateHelper
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthViewModel(private val userRepository: UserRepository, private val appRepository: AppRepository) : BaseViewModel<AuthState, AuthEvent>() {
    override val reducer = AuthReducer(AuthState.initial())

    override val state: StateFlow<AuthState>
        get() = reducer.state

    var rememberEmail: String? = null

    override fun onClearLoadingState() {
    }

    override fun onClearErrorState() {
    }

    override val onException: ((Throwable) -> Unit) = {
        reducer.sendEvent(AuthEvent.LoginFail(it))
    }

    fun login(email: String, password: String) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {

            val emailError = ValidateHelper.validateEmail(email)
            val passwordError = ValidateHelper.validatePassword(password)

            if (emailError.isNullOrEmpty() && passwordError.isNullOrEmpty()) {
                reducer.sendEvent(AuthEvent.Loading(true))

                userRepository.login(email, password).collect {
                    WholeApp.USER_ID = it.id
                    appRepository.setLoggedIn(true)

                    runBlocking {
                        appRepository.setUserId(it.id)
                    }

                    reducer.sendEvent(AuthEvent.NavigateMain)
                }
            }
        }
    }

    fun signUp(email: String, password: String, name: String, dateOfBirth: String, gender: String, isPrivacyAgree: Boolean) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            val emailError = ValidateHelper.validateEmail(email)
            val passwordError = ValidateHelper.validatePassword(password)
            val nameError = if (name.isBlank()) "Name is required" else null

            if (emailError.isNullOrEmpty() && passwordError.isNullOrEmpty() && nameError.isNullOrEmpty() && isPrivacyAgree) {
                reducer.sendEvent(AuthEvent.Loading(true))
                userRepository.signUp(email, password, name, dateOfBirth, gender).collect {
                    if (it) {
                        reducer.sendEvent(AuthEvent.SignUpSuccess)
                        rememberEmail = email
                    } else reducer.sendEvent(AuthEvent.SignUpFail(Exception("Sign up fail")))
                }
            }
        }
    }

}

class AuthReducer(initialVal: AuthState) : Reducer<AuthState, AuthEvent>(initialVal) {

    override fun reduce(oldState: AuthState, event: AuthEvent) {
        when (event) {
            is AuthEvent.LoginSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, currentScreen = AuthCurrentScreen.MAIN))
            }

            is AuthEvent.Loading -> {
                setState(oldState.copy(isLoading = true, error = null))
            }

            is AuthEvent.LoginFail -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true)))
            }

            AuthEvent.DisplayLogin -> {
                setState(oldState.copy(isLoading = false, error = null, currentScreen = AuthCurrentScreen.LOGIN))
            }

            AuthEvent.DisplaySignUp -> {
                setState(oldState.copy(isLoading = false, error = null, currentScreen = AuthCurrentScreen.SIGNUP))
            }

            AuthEvent.NavigateMain -> {
                setState(oldState.copy(isLoading = false, error = null, currentScreen = AuthCurrentScreen.MAIN))
            }

            is AuthEvent.SignUpFail -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true)))
            }

            is AuthEvent.SignUpSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, currentScreen = AuthCurrentScreen.LOGIN))
            }
        }
    }
}
