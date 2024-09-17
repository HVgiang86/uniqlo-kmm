package com.gianghv.uniqlo.presentation.screen.auth.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gianghv.uniqlo.base.getViewModel
import com.gianghv.uniqlo.presentation.screen.auth.AuthViewModel
import com.gianghv.uniqlo.presentation.screen.auth.login.LoginScreen
import com.gianghv.uniqlo.presentation.screen.auth.signup.SignUpScreen
import com.gianghv.uniqlo.util.logging.AppLogger

interface LoginDestination {
    object SignUp : Screen, LoginDestination {
        @Composable
        override fun Content() {
            val viewModel: AuthViewModel = getViewModel()
            SignUpScreen(viewModel = viewModel)
        }
    }

    object Login : Screen, LoginDestination {
        @Composable
        override fun Content() {
            val viewModel: AuthViewModel = getViewModel()
            LoginScreen(viewModel = viewModel)
        }

    }
}

@Composable
fun LoginNavigation(destination: LoginDestination) {
    Navigator(screen = destination as Screen)
}
