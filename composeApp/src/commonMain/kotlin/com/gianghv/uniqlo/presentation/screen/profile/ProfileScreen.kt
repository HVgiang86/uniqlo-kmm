package com.gianghv.uniqlo.presentation.screen.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.presentation.screen.profile.components.SettingBottomSheet
import com.gianghv.uniqlo.util.asState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navigateTo: (MainScreenDestination) -> Unit, onLogout: () -> Unit) {
    val state by viewModel.state.asState()

    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(
        initialDetent = Hidden, detents = listOf(Hidden, FullyExpanded)
    )

    LaunchedEffect(Unit) {
        viewModel.sendEvent(ProfileUiEvent.LoadUser)
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = { })
    }

    if (state.isLogout) {
        onLogout()
    }


    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Profile", style = MaterialTheme.typography.titleLarge) }, actions = {
            IconButton(onClick = {
                scope.launch {
                    bottomSheetState.animateTo(FullyExpanded)
                }
            }) {
                com.composables.core.Icon(
                    imageVector = Icons.Default.Settings, contentDescription = null, tint = Color.Black
                )
            }

            IconButton(onClick = {
                viewModel.sendEvent(ProfileUiEvent.Logout)
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
            }
        })
    }) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            Text(text = "Profile id = ${state.user?.id}", modifier = Modifier.align(Alignment.Center))
        }

        SettingBottomSheet(state = bottomSheetState, onChangeRecommendationServer = {
            viewModel.sendEvent(ProfileUiEvent.ChangeRecommendationServer(it))
        }, onChangeChatServer = {
            viewModel.sendEvent(ProfileUiEvent.ChangeChatServer(it))
        })
    }

}
