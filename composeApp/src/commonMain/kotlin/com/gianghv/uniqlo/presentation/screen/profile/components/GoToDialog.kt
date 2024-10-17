package com.gianghv.uniqlo.presentation.screen.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogState
import com.gianghv.uniqlo.presentation.component.AppOutlinedTextField
import com.gianghv.uniqlo.presentation.component.RedFilledTextButton

@Composable
fun GoToDialogDeepLink(dialogState: DialogState, onGoTo: (String) -> Unit) {
    val textState = rememberSaveable { mutableStateOf("") }

    Dialog(state = dialogState) {
        DialogPanel(
            modifier = Modifier.displayCutoutPadding().systemBarsPadding().widthIn(min = 280.dp, max = 560.dp).padding(20.dp).clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFE4E4E4), RoundedCornerShape(12.dp)).background(Color.White),
        ) {
            Column {
                Column(Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)) {
                    BasicText(
                        text = "Go to a Product Deeplink", style = TextStyle(fontWeight = FontWeight.Medium)
                    )
                    Spacer(Modifier.height(8.dp))
                    AppOutlinedTextField(
                        initialValue = "uniqlo://product?id=",
                        onValueChange = {
                            textState.value = it
                        },
                        placeholder = "Product Deeplink",
                        modifier = Modifier.height(60.dp),
                        maxLines = 1,
                        onMessageSent = {
                            onGoTo(textState.value)
                        }
                    )
                }
                Spacer(Modifier.height(24.dp))
                Box(Modifier.padding(12.dp).align(Alignment.End).clip(RoundedCornerShape(4.dp)).clickable(role = Role.Button) { /* TODO */ }
                    .padding(horizontal = 12.dp, vertical = 8.dp)) {
                    RedFilledTextButton(onClick = {
                        onGoTo(textState.value)
                    }, text = {
                        BasicText(text = "Go!", style = TextStyle(color = Color.White))
                    }, modifier = Modifier.align(Alignment.CenterEnd))
                }
            }
        }
    }
}

@Composable
fun GoToProductDialog(dialogState: DialogState, onGoTo: (Long) -> Unit) {
    val textState = rememberSaveable { mutableStateOf("") }

    Dialog(state = dialogState) {
        DialogPanel(
            modifier = Modifier.displayCutoutPadding().systemBarsPadding().widthIn(min = 280.dp, max = 560.dp).padding(20.dp).clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFE4E4E4), RoundedCornerShape(12.dp)).background(Color.White),
        ) {
            Column {
                Column(Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)) {
                    BasicText(
                        text = "Go to a Product Id", style = TextStyle(fontWeight = FontWeight.Medium)
                    )
                    Spacer(Modifier.height(8.dp))
                    AppOutlinedTextField(
                        initialValue = "",
                        onValueChange = {
                            textState.value = it
                        },
                        placeholder = "Product Deeplink",
                        modifier = Modifier.height(60.dp),
                        maxLines = 1,
                        onMessageSent = {
                            val id = textState.value.toLongOrNull()
                            id?.let { it1 -> onGoTo(it1) }
                        }
                    )
                }
                Spacer(Modifier.height(24.dp))
                Box(Modifier.padding(12.dp).align(Alignment.End).clip(RoundedCornerShape(4.dp)).clickable(role = Role.Button) { /* TODO */ }
                    .padding(horizontal = 12.dp, vertical = 8.dp)) {
                    RedFilledTextButton(onClick = {
                        val id = textState.value.toLongOrNull()
                        id?.let { onGoTo(it) }
                    }, text = {
                        BasicText(text = "Go!", style = TextStyle(color = Color.White))
                    }, modifier = Modifier.align(Alignment.CenterEnd))
                }
            }
        }
    }
}
