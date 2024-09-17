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
        AuthCurrentScreen.LOGIN -> {LoginNavigation(LoginDestination.Login)
            AppLogger.d("change current screen to login")}
        AuthCurrentScreen.SIGNUP -> {LoginNavigation(LoginDestination.SignUp)
            AppLogger.d("change current screen to login")}
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
