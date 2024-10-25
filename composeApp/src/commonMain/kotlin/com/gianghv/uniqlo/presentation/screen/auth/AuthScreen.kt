package com.gianghv.uniqlo.presentation.screen.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.screen.auth.navigation.LoginDestination
import com.gianghv.uniqlo.presentation.screen.auth.navigation.LoginNavigation
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.logging.AppLogger

@Composable
fun AuthScreen(viewModel: AuthViewModel, onNavigateMain: () -> Unit) {
    val state by viewModel.state.asState()

    when(state.currentScreen) {
        AuthCurrentScreen.LOGIN -> {LoginNavigation(LoginDestination.Login)}
        AuthCurrentScreen.SIGNUP -> {LoginNavigation(LoginDestination.SignUp)}
        AuthCurrentScreen.MAIN -> {
            onNavigateMain()
        }
    }

    if (state.error?.shouldShowDialog == true) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = {})
    }

    if (state.isLoading) {
        LoadingDialog()
    }
}
