package com.gianghv.uniqlo.presentation.screen.aichat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.ChatOutlinedTextField
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.screen.aichat.components.ChatItem
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.util.asState
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import uniqlo.composeapp.generated.resources.Res

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun AIChatScreen(viewModel: AiChatViewModel, navigateTo: (MainScreenDestination) -> Unit) {
    val state by viewModel.state.asState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(state.chatMessages) {
        coroutineScope.launch {
            if (state.chatMessages.isNotEmpty()) listState.animateScrollToItem(state.chatMessages.size - 1)
        }
    }

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
    }) { scaffoldPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(scaffoldPadding).padding(bottom = 80.dp).consumeWindowInsets(scaffoldPadding).systemBarsPadding()
                .imePadding()
        ) {
            // Chat content
            val chats = state.chatMessages

            var boxWidth by remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current

            if (chats.isNotEmpty()) {
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(bottom = 52.dp).onGloballyPositioned { layoutCoordinates ->
                    val widthInPx = layoutCoordinates.size.width
                    boxWidth = with(density) { widthInPx.toDp() }
                }) {
                    val listSize = if (state.isServerTyping) (chats.size + 1) else chats.size
                    items(listSize) { index ->
                        if (index == chats.size) {
                            val composition by rememberLottieComposition {
                                LottieCompositionSpec.JsonString(
                                    Res.readBytes("files/typinganim.json").decodeToString()
                                )
                            }

                            Image(
                                painter = rememberLottiePainter(
                                    composition = composition, iterations = Compottie.IterateForever
                                ), contentDescription = "Lottie animation", modifier = Modifier.size(80.dp).align(Alignment.CenterStart)
                            )

                        } else {
                            ChatItem(chatMessage = chats[index], chats[index].products, boxWidth, onProductClick = {
                                navigateTo(MainScreenDestination.ProductDetail(mapOf(MainScreenDestination.ProductDetail.PRODUCT_ID_KEY to it)))
                            }, onLoadEmbeddedProduct = {
                                it.forEach { it1 ->
                                    viewModel.sendEvent(AiChatUiEvent.LoadEmbeddedProduct(id = it1, messageId = chats[index].id))
                                }
                            })
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.padding(scaffoldPadding).fillMaxSize()) {
                    Text(text = "Không có tin nhắn nào!", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.titleMedium)
                }
            }

            val textState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue())
            }

            if (!state.serverError) {
                TypePanel(textState = textState, viewModel = viewModel, keyboardController = keyboardController)
            }

        }
    }
}


@Composable
fun BoxScope.TypePanel(textState: MutableState<TextFieldValue>, viewModel: AiChatViewModel, keyboardController: SoftwareKeyboardController?) {
    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.BottomCenter).background(color = Color.White)) {
        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp), thickness = 1.dp, color = Color.LightGray)
        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            ChatOutlinedTextField(
                modifier = Modifier.height(52.dp).fillMaxWidth().padding(start = 8.dp, end = 24.dp),
                placeholder = "Nhập tin nhắn...",
                textState = textState,
                onValueChange = {},
                onMessageSent = {
                    viewModel.sendEvent(AiChatUiEvent.SendMessage(it))
                    textState.value = TextFieldValue("")
                },
                imeAction = ImeAction.Send,
                shape = RoundedCornerShape(24.dp)
            )

            IconButton(modifier = Modifier.size(46.dp).padding(end = 8.dp).align(Alignment.CenterEnd), onClick = {
                viewModel.sendEvent(AiChatUiEvent.SendMessage(textState.value.text))
                textState.value = TextFieldValue("")
                keyboardController?.hide()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
            }

        }
    }
}
