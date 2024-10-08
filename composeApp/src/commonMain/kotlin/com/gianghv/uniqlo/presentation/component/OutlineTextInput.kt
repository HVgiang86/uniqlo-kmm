package com.gianghv.uniqlo.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.theme.Typography
import com.gianghv.uniqlo.theme.icons.Visibility
import com.gianghv.uniqlo.theme.icons.VisibilityOff
import com.gianghv.uniqlo.util.ValidateHelper
import com.gianghv.uniqlo.util.logging.AppLogger
import org.jetbrains.compose.resources.stringResource
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.textfield_desc

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppOutlinedTextField(
    modifier: Modifier,
    validator: ValidateHelper.(String) -> String? = { null },
    inputWrapper: MutableState<InputWrapper> = remember { mutableStateOf(InputWrapper()) },
    placeholder: String,
    initialValue: String = "",
    maxLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: ((String) -> Unit)? = null,
    resetScroll: () -> Unit = {},
    imeAction: ImeAction = ImeAction.Done,
    onMessageSent: (String) -> Unit,
    shape: Shape = RoundedCornerShape(4.dp),
    textStyle: TextStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
    onClick: () -> Unit = {}
) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(initialValue))
    }

    // Used to decide if the keyboard should be shown
    var textFieldFocusState by remember { mutableStateOf(false) }

    Column {
        UserInputTextOutlined(
            onTextChanged = {
                textState = it
                inputWrapper.value = inputWrapper.value.updateValue(it.text, true)
                inputWrapper.value = inputWrapper.value.validate { input ->
                    validator(input)
                }

                if (inputWrapper.value.isValid) {
                    onValueChange?.invoke(it.text)
                }
            },

            hint = placeholder,
            description = stringResource(Res.string.textfield_desc),
            textFieldValue = textState,
            keyboardShown = textFieldFocusState,
            onTextFieldFocused = { focused ->
                if (focused) {
                    resetScroll()
                }
                textFieldFocusState = focused

            },
            onMessageSent = {
                onMessageSent(textState.text)

                textFieldFocusState = false

                // Move scroll to bottom
                resetScroll()
            },
            focusState = textFieldFocusState,
            imeAction = imeAction,
            shape = shape,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            modifier = modifier.height(52.dp),
            maxLines = 1,
            textStyle = textStyle,
            onClick = onClick
        )

        if (!inputWrapper.value.isValid) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = inputWrapper.value.errorString ?: "", modifier = Modifier.padding(horizontal = 2.dp), style = Typography.bodySmall, color = Color.Red
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppPasswordField(
    modifier: Modifier,
    validator: ValidateHelper.(String) -> String? = { null },
    inputWrapper: MutableState<InputWrapper> = remember { mutableStateOf(InputWrapper()) },
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: ((String) -> Unit)? = null,
    resetScroll: () -> Unit = {},
    imeAction: ImeAction = ImeAction.Done,
    onMessageSent: (String) -> Unit,
    shape: Shape = RoundedCornerShape(4.dp),
    textStyle: TextStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
    maxLines: Int = 1,
    onClick: () -> Unit = {}
) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    var passwordVisible by remember { mutableStateOf(false) }
    val visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()

    // Used to decide if the keyboard should be shown
    var textFieldFocusState by remember { mutableStateOf(false) }

    Column {
        UserInputTextOutlined(onTextChanged = {
            textState = it
            inputWrapper.value = inputWrapper.value.updateValue(it.text, true)
            inputWrapper.value = inputWrapper.value.validate { input ->
                validator(input)
            }

            if (inputWrapper.value.isValid) {
                onValueChange?.invoke(it.text)
            }
        },
            keyboardType = KeyboardType.Password,
            hint = placeholder,
            description = stringResource(Res.string.textfield_desc),
            textFieldValue = textState,
            keyboardShown = textFieldFocusState,
            onTextFieldFocused = { focused ->
                if (focused) {
                    resetScroll()
                }
                textFieldFocusState = focused

            },
            onMessageSent = {
                onMessageSent(textState.text)

                // Move scroll to bottom
                resetScroll()
                textFieldFocusState = false
            },
            focusState = textFieldFocusState,
            imeAction = imeAction,
            shape = shape,
            leadingIcon = leadingIcon,
            modifier = modifier.height(52.dp),
            visualTransformation = visualTransformation,
            maxLines = maxLines,
            trailingIcon = {
                val image = if (passwordVisible) Visibility else VisibilityOff
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            textStyle = textStyle,
            onClick = onClick
        )

        if (!inputWrapper.value.isValid) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = inputWrapper.value.errorString ?: "", modifier = Modifier.padding(horizontal = 2.dp), style = Typography.bodySmall, color = Color.Red
            )
        }
    }
}

data class InputWrapper(
    var value: String = "", var errorString: String? = null
) {
    val isValid: Boolean
        get() = errorString == null

    fun updateValue(value: String, shouldClearError: Boolean = true): InputWrapper {
        return copy(value = value, errorString = if (shouldClearError) null else errorString)
    }

    fun validate(block: ValidateHelper.(String) -> String?): InputWrapper {
        val result = block.invoke(ValidateHelper, value)
        return copy(errorString = result)
    }
}

@ExperimentalFoundationApi
@Composable
fun UserInputTextOutlined(
    keyboardType: KeyboardType = KeyboardType.Text,
    hint: String,
    description: String,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    keyboardShown: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    onMessageSent: (String) -> Unit,
    focusState: Boolean,
    imeAction: ImeAction = ImeAction.Done,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            BaseOutlinedTextField(textFieldValue,
                hint,
                onTextChanged,
                onTextFieldFocused,
                keyboardType,
                focusState,
                imeAction,
                onMessageSent,
                Modifier.semantics {
                    contentDescription = description
                    keyboardShownProperty = keyboardShown
                },
                maxLines = maxLines,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                shape = shape,
                visualTransformation = visualTransformation,
                textStyle = textStyle,
                onClick = onClick
            )
        }
    }
}

@Composable
fun BoxScope.BaseOutlinedTextField(
    textFieldValue: TextFieldValue,
    hint: String,
    onTextChanged: (TextFieldValue) -> Unit,
    onTextFieldFocused: (Boolean) -> Unit,
    keyboardType: KeyboardType,
    focusState: Boolean,
    imeAction: ImeAction = ImeAction.Done,
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
    onClick: () -> Unit = {},
) {
    var lastFocusState by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = onTextChanged,
        modifier = modifier.fillMaxWidth().align(Alignment.CenterStart).onFocusChanged { state ->
            if (state.isFocused) {
                AppLogger.d("click")
                onClick()
            }
            if (lastFocusState != state.isFocused) {
                onTextFieldFocused(state.isFocused)
            }
            lastFocusState = state.isFocused
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType, imeAction = imeAction
        ),
        keyboardActions = KeyboardActions {
            if (textFieldValue.text.isNotBlank()) {
                onMessageSent(textFieldValue.text)
                keyboardController?.hide()
            }
        },
        maxLines = maxLines,
        singleLine = maxLines == 1,
        textStyle = textStyle,
        shape = shape,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation
    )

    val disableContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    if (textFieldValue.text.isEmpty() && !focusState) {
        val startPadding = if (leadingIcon != null) 40.dp else 16.dp
        val endPadding = if (trailingIcon != null) 40.dp else 16.dp

        val modifierT = Modifier.align(Alignment.CenterStart).padding(start = startPadding, end = endPadding)

        Text(
            modifier = modifierT, maxLines = maxLines, text = hint, style = textStyle.copy(color = disableContentColor)
        )
    }
}
