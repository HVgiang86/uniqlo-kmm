package com.gianghv.uniqlo.rootview.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gianghv.uniqlo.base.getViewModel
import com.gianghv.uniqlo.presentation.screen.login.LoginScreen
import com.gianghv.uniqlo.presentation.screen.login.LoginViewModel
import com.gianghv.uniqlo.presentation.screen.main.MainScreen
import com.gianghv.uniqlo.presentation.screen.main.MainViewModel
import com.gianghv.uniqlo.presentation.screen.onboarding.OnBoardingScreen


interface RootAppDestination {
    object Home : Screen, RootAppDestination {
        @Composable
        override fun Content() {
            val mainViewModel: MainViewModel = getViewModel()
            MainScreen(viewModel = mainViewModel)
        }

    }

    object Login : Screen, RootAppDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val loginViewModel: LoginViewModel = getViewModel()
            LoginScreen(viewModel = loginViewModel, onNavigateMain = {
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
