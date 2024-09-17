package com.gianghv.uniqlo.rootview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.data.AppRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.component.MyAlertDialog
import com.gianghv.uniqlo.rootview.navigation.RootAppDestination
import com.gianghv.uniqlo.rootview.navigation.RootAppNavigation
import com.gianghv.uniqlo.theme.AppTheme
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_cyclone

@Composable
fun App(modifier: Modifier = Modifier) = AppTheme {
    val state = remember { mutableStateOf(RootAppUiState.initial()) }
    val appRepository: AppRepository = koinInject()
    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        val isFirstRun = appRepository.isFirstRun()
        if (isFirstRun) {
            AppLogger.d("First run")
            state.value = state.value.copy(isLoading = false, error = null, isFirstRun = true, isLoggedIn = false)
        } else {
            AppLogger.d("Not first run")
            val isLoggedIn = appRepository.isLoggedIn()
            AppLogger.d("Is logged in: $isLoggedIn")

            if (isLoggedIn) {
                val userId = appRepository.getUserId()
                if (userId != null) {
                    AppLogger.d("User id: $userId")
                    state.value = state.value.copy(isLoading = true, error = null, isFirstRun = false, isLoggedIn = true)
                    WholeApp.USER_ID = userId
                }
            } else {
                state.value = state.value.copy(isLoading = false, error = null, isFirstRun = false, isLoggedIn = false)
            }
        }
    }

    if (state.value.error != null) {
        //show error dialog
        if (state.value.error?.shouldShowDialog == true) {
            AppLogger.d("Has error: ${state.value.error?.throwable} ${state.value.error?.shouldShowDialog}")
            MyAlertDialog(title = "Thông báo", content = state.value.error?.throwable?.message ?: "Unknown error!", rightBtnTitle = "OK", rightBtn = {})
        }
    }

    if (state.value.isLoading) {
        LoadingDialog()
    }

    if (state.value.isFirstRun) {
        RootAppNavigation(startDestination = RootAppDestination.OnBoarding)
    }

    if (state.value.isLoggedIn) {
        RootAppNavigation(startDestination = RootAppDestination.Home)
    }

    if (!state.value.isLoggedIn && !state.value.isFirstRun) {
        RootAppNavigation(startDestination = RootAppDestination.Login)
    }
}

@Composable
private fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(Res.drawable.ic_cyclone), contentDescription = null, modifier = Modifier.size(180.dp)
        )
    }
}

