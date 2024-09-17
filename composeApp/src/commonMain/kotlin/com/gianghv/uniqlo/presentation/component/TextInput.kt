package com.gianghv.uniqlo.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import org.jetbrains.compose.resources.stringResource
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.textfield_desc
import uniqlo.composeapp.generated.resources.textfield_hint

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserInputField(
    onMessageSent: (String) -> Unit, modifier: Modifier = Modifier, resetScroll: () -> Unit = {}, imeAction: ImeAction = ImeAction.Done
) {

    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    // Used to decide if the keyboard should be shown
    var textFieldFocusState by remember { mutableStateOf(false) }

    Surface(tonalElevation = 2.dp, contentColor = MaterialTheme.colorScheme.secondary) {
        Column(modifier = modifier) {
            UserInputTextT(textFieldValue = textState, onTextChanged = { textState = it },
                // Only show the keyboard if there's no input selector and text field has focus
                keyboardShown = textFieldFocusState,
                // Close extended selector if text field receives focus
                onTextFieldFocused = { focused ->
                    if (focused) {
                        resetScroll()
                    }
                    textFieldFocusState = focused

                }, onMessageSent = {
                    onMessageSent(textState.text)
                    // Reset text field and close keyboard
                    textState = TextFieldValue()
                    // Move scroll to bottom
                    resetScroll()
                }, focusState = textFieldFocusState, imeAction = imeAction
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun UserInputTextT(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    keyboardShown: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    onMessageSent: (String) -> Unit,
    focusState: Boolean,
    imeAction: ImeAction = ImeAction.Done
) {
    val a11ylabel = stringResource(Res.string.textfield_desc)
    Row(
        modifier = Modifier.fillMaxWidth().height(64.dp), horizontalArrangement = Arrangement.End
    ) {
        Box(Modifier.fillMaxSize()) {
            BaseTextField(textFieldValue, onTextChanged, onTextFieldFocused, keyboardType, focusState, imeAction, onMessageSent, Modifier.semantics {
                contentDescription = a11ylabel
                keyboardShownProperty = keyboardShown
            })
        }
    }
}

private fun TextFieldValue.addText(newString: String): TextFieldValue {
    val newText = this.text.replaceRange(
        this.selection.start, this.selection.end, newString
    )
    val newSelection = TextRange(
        start = newText.length, end = newText.length
    )

    return this.copy(text = newText, selection = newSelection)
}

@Composable
fun BoxScope.BaseTextField(
    textFieldValue: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    onTextFieldFocused: (Boolean) -> Unit,
    keyboardType: KeyboardType,
    focusState: Boolean,
    imeAction: ImeAction = ImeAction.Done,
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var lastFocusState by remember { mutableStateOf(false) }


    BasicTextField(value = textFieldValue,
        onValueChange = { onTextChanged(it) },
        modifier = modifier.padding(start = 32.dp).align(Alignment.CenterStart).onFocusChanged { state ->
            if (lastFocusState != state.isFocused) {
                onTextFieldFocused(state.isFocused)
            }
            lastFocusState = state.isFocused
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType, imeAction = imeAction
        ),
        keyboardActions = KeyboardActions {
            if (textFieldValue.text.isNotBlank()) onMessageSent(textFieldValue.text)
        },
        maxLines = 1,
        cursorBrush = SolidColor(LocalContentColor.current),
        textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
    )

    val disableContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    if (textFieldValue.text.isEmpty() && !focusState) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp),
            text = stringResource(Res.string.textfield_hint),
            style = MaterialTheme.typography.bodyLarge.copy(color = disableContentColor)
        )
    }
}


