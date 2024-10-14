package com.gianghv.uniqlo.rootview.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gianghv.uniqlo.base.getViewModel
import com.gianghv.uniqlo.presentation.screen.auth.AuthScreen
import com.gianghv.uniqlo.presentation.screen.auth.login.LoginScreen
import com.gianghv.uniqlo.presentation.screen.auth.AuthViewModel
import com.gianghv.uniqlo.presentation.screen.main.MainScreen
import com.gianghv.uniqlo.presentation.screen.main.MainViewModel
import com.gianghv.uniqlo.presentation.screen.onboarding.OnBoardingScreen


interface RootAppDestination {
    object Home : Screen, RootAppDestination {
        @Composable
        override fun Content() {
            val mainViewModel: MainViewModel = getViewModel()
            val navigator = LocalNavigator.currentOrThrow
            MainScreen(viewModel = mainViewModel, onLogout = {
                navigator.replace(Login)
            })
        }
    }

    object Login : Screen, RootAppDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val loginViewModel: AuthViewModel = getViewModel()
            AuthScreen(viewModel = loginViewModel, onNavigateMain = {
                navigator.replace(Home)
            })
        }
    }

    object OnBoarding : Screen, RootAppDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            OnBoardingScreen(onNavigateMain = {
                navigator.replace(Login)
            })
        }
    }
}

@Composable
fun RootAppNavigation(startDestination: RootAppDestination) {
    Navigator(screen = startDestination as Screen)
}
