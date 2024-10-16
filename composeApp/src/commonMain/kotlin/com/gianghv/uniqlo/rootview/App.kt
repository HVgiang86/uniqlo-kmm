package com.gianghv.uniqlo.rootview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.gianghv.uniqlo.data.AppRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.rootview.navigation.RootAppDestination
import com.gianghv.uniqlo.rootview.navigation.RootAppNavigation
import com.gianghv.uniqlo.theme.AppTheme
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun App(productId: Long? = null) = AppTheme {
    val state = remember { mutableStateOf(RootAppUiState.initial()) }
    val appRepository: AppRepository = koinInject()
    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        val recommendationUrl = appRepository.getRecommendationUrl()
        if (recommendationUrl != null) {
            WholeApp.RECOMMEND_BASE_URL = recommendationUrl
        } else {
            appRepository.setRecommendationUrl(WholeApp.RECOMMEND_BASE_URL)
        }

        val chatUrl = appRepository.getChatUrl()
        if (chatUrl != null) {
            WholeApp.CHAT_BASE_URL = chatUrl
        } else {
            appRepository.setChatUrl(WholeApp.CHAT_BASE_URL)
        }

        val isFirstRun = appRepository.isFirstRun()
        if (isFirstRun) {
            state.value = state.value.copy(isLoading = false, error = null, isFirstRun = true, isLoggedIn = false)
        } else {
            val isLoggedIn = appRepository.isLoggedIn()
            if (isLoggedIn) {
                val userId = appRepository.getUserId()
                AppLogger.d("User id: $userId")
                if (userId != null && userId != -1L) {
                    state.value = state.value.copy(isLoading = false, error = null, isFirstRun = false, isLoggedIn = true)
                    WholeApp.USER_ID = userId
                }
            } else {
                state.value = state.value.copy(isLoading = false, error = null, isFirstRun = false, isLoggedIn = false)
            }
        }
    }

    if (state.value.error != null) {
        //show error dialog
        AppErrorDialog(state.value.error?.throwable, onDismissRequest = {
            state.value = state.value.copy(error = null)
        })
    }

    if (state.value.isLoading) {
        LoadingDialog()
    }

    if (state.value.isFirstRun) {
        RootAppNavigation(startDestination = RootAppDestination.OnBoarding)
    }

    if (state.value.isLoggedIn) {
        AppLogger.d("Logged in. Process to product detail screen: $productId")
        RootAppNavigation(startDestination = RootAppDestination.Home)
    }

    if (!state.value.isLoggedIn && !state.value.isFirstRun) {
        RootAppNavigation(startDestination = RootAppDestination.Login)
    }
}
