package com.gianghv.uniqlo.presentation.screen.aichat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.screen.aichat.components.ChatItem
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.util.asState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIChatScreen(viewModel: AiChatViewModel, navigateTo: (MainScreenDestination) -> Unit) {
    val state by viewModel.state.asState()

    LaunchedEffect(Unit) {
        viewModel.sendEvent(AiChatUiEvent.LoadChatMessages)
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = { })
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "AI Chat")
        })
    }) {
        // Chat content
        val chats = state.chatMessages

        var boxWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        if (chats.isNotEmpty()) {
            LazyColumn(modifier = Modifier.padding(top = it.calculateTopPadding(), bottom = 80.dp).fillMaxSize().onGloballyPositioned { layoutCoordinates ->
                val widthInPx = layoutCoordinates.size.width
                boxWidth = with(density) { widthInPx.toDp() }
            }) {
                items(chats.size) { index ->
                    ChatItem(chatMessage = chats[index], boxWidth)
                }
            }
        } else {
            Box(modifier = Modifier.padding(it).fillMaxSize()) {
                Text(text = "No messages", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.titleMedium)
            }

        }
    }

}
