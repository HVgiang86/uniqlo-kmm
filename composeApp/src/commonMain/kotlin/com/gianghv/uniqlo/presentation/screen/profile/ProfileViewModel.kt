package com.gianghv.uniqlo.presentation.screen.profile

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.AppRepository
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.WholeApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository, private val appRepository: AppRepository) : BaseViewModel<ProfileUiState, ProfileUiEvent>() {
    override val state: StateFlow<ProfileUiState>
        get() = reducer.state
    override val reducer: Reducer<ProfileUiState, ProfileUiEvent>
        get() = ProfileReducer.getInstance(ProfileUiState.initial(), this)

    override fun onClearLoadingState() {

    }

    override fun onClearErrorState() {

    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(ProfileUiEvent.Error(it))
        }

    fun getMyProfile() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            userRepository.getMyProfile(WholeApp.USER_ID).collect {
                reducer.sendEvent(ProfileUiEvent.LoadUserSuccess(it))
            }
        }
    }

    fun logout() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            appRepository.setLoggedIn(false)
            appRepository.setUserId(-1L)
            reducer.sendEvent(ProfileUiEvent.LogoutSuccess)
        }
    }
}


class ProfileReducer(initialUiState: ProfileUiState, private val viewModel: ProfileViewModel) : Reducer<ProfileUiState, ProfileUiEvent>(initialUiState) {
    override fun reduce(oldState: ProfileUiState, event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error)))
            }

            ProfileUiEvent.LoadUser -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.getMyProfile()
            }

            is ProfileUiEvent.LoadUserSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, user = event.user))
            }

            ProfileUiEvent.Logout -> {
                setState(oldState.copy(isLoading = true, error = null))
                viewModel.logout()
            }

            ProfileUiEvent.LogoutSuccess -> {
                setState(oldState.copy(isLoading = false, error = null, isLogout = true))
            }
        }
    }

    companion object {
        private var INSTANCE: ProfileReducer? = null
        fun getInstance(initialUiState: ProfileUiState, viewModel: ProfileViewModel): ProfileReducer {
            if (INSTANCE == null) {
                INSTANCE = ProfileReducer(initialUiState, viewModel)
            }
            return INSTANCE!!
        }
    }

}
