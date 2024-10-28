package com.gianghv.uniqlo.presentation.screen.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Sheet
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.AppPasswordField
import com.gianghv.uniqlo.presentation.component.InputWrapper
import com.gianghv.uniqlo.presentation.component.RedFilledTextButton

@Composable
fun ChangePassBottomSheet(
    state: ModalBottomSheetState? = null, onChangePassword: (String, String) -> Unit = { oldPass, newPass -> }
) {
    val show = state ?: rememberModalBottomSheetState(
        initialDetent = FullyExpanded, detents = listOf(Hidden, FullyExpanded)
    )

    val currentPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    val errorThrowable = remember { mutableStateOf<Throwable?>(null) }
    val buttonThrowable = remember { mutableStateOf<Throwable?>(null) }

    ModalBottomSheet(state = show) {
        Sheet(
            modifier = Modifier.padding(top = 12.dp).shadow(8.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(Color.White).widthIn(max = 640.dp).fillMaxWidth().imePadding(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
            ) {
                DragIndication(
                    modifier = Modifier.padding(top = 22.dp).align(Alignment.CenterHorizontally).background(Color.Black.copy(0.4f), RoundedCornerShape(100))
                        .width(32.dp).height(4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Change password", style = MaterialTheme.typography.titleSmall, color = Color.Black)
                Text("Current password", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

                val currentPasswordInputWrapper = remember { mutableStateOf(InputWrapper()) }
                AppPasswordField(modifier = Modifier.fillMaxWidth().height(52.dp),
                    inputWrapper = currentPasswordInputWrapper,
                    placeholder = "Current password",
                    onMessageSent = {
                        currentPassword.value = it
                    },
                    shape = RoundedCornerShape(10.dp),
                    imeAction = ImeAction.Done,
                    onValueChange = {
                        currentPassword.value = it
                    },
                    validator = {
                        validatePassword(it)
                    })

                Spacer(modifier = Modifier.height(16.dp))

                Text("New password", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

                val newPasswordInputWrapper = remember { mutableStateOf(InputWrapper()) }
                AppPasswordField(modifier = Modifier.fillMaxWidth().height(52.dp),
                    inputWrapper = newPasswordInputWrapper,
                    placeholder = "New password",
                    onMessageSent = {
                        newPassword.value = it
                    },
                    shape = RoundedCornerShape(10.dp),
                    imeAction = ImeAction.Done,
                    onValueChange = {
                        newPassword.value = it
                    },
                    validator = {
                        validatePassword(it)
                    })

                Spacer(modifier = Modifier.height(16.dp))

                Text("Confirm new password", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

                val confirmPasswordInputWrapper = remember { mutableStateOf(InputWrapper()) }
                AppPasswordField(modifier = Modifier.fillMaxWidth().height(52.dp),
                    inputWrapper = confirmPasswordInputWrapper,
                    placeholder = "Confirm password",
                    onMessageSent = {
                        confirmPassword.value = it
                    },
                    shape = RoundedCornerShape(10.dp),
                    imeAction = ImeAction.Done,
                    onValueChange = {
                        confirmPassword.value = it
                    },
                    validator = {
                        validatePassword(it)
                    })

                Spacer(modifier = Modifier.height(32.dp))

                RedFilledTextButton(onClick = {
                    val oldPass = currentPassword.value
                    val newPass = newPassword.value
                    val confirmPass = confirmPassword.value

                    if (oldPass.isBlank() || newPass.isBlank() || confirmPass.isBlank()) {
                        errorThrowable.value = Exception("Please fill all fields")
                        return@RedFilledTextButton
                    }

                    if (newPass != confirmPass) {
                        errorThrowable.value = Exception("New password and confirm password do not match")
                        return@RedFilledTextButton
                    }

                    if (newPass == oldPass) {
                        errorThrowable.value = Exception("New password must be different from the old password")
                        return@RedFilledTextButton
                    }

                    buttonThrowable.value = Exception("Quá lười để làm tính năng này. Bỏ nhé :))")
                    errorThrowable.value = null
                }, text = {
                    Text("Save", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                })

                Spacer(modifier = Modifier.height(32.dp))

                AppErrorDialog(throwable = errorThrowable.value, onDismissRequest = {
                    errorThrowable.value = null
                })

                AppErrorDialog(throwable = buttonThrowable.value, onDismissRequest = {
                    buttonThrowable.value = null
                    onChangePassword(currentPassword.value, newPassword.value)
                })
            }
        }
    }
}
