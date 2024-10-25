package com.gianghv.uniqlo.presentation.screen.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.gianghv.uniqlo.constant.DD_MM_YYYY
import com.gianghv.uniqlo.presentation.component.AppDatePicker
import com.gianghv.uniqlo.presentation.component.AppOutlinedTextField
import com.gianghv.uniqlo.presentation.component.AppPasswordField
import com.gianghv.uniqlo.presentation.component.BlackButtonIconEnd
import com.gianghv.uniqlo.presentation.screen.auth.AuthEvent
import com.gianghv.uniqlo.presentation.screen.auth.AuthViewModel
import com.gianghv.uniqlo.presentation.screen.auth.login.UniqloHeader
import com.gianghv.uniqlo.theme.icons.Calendar
import com.gianghv.uniqlo.util.ext.millisToDateString
import com.gianghv.uniqlo.util.logging.AppLogger

@OptIn(InternalVoyagerApi::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier.fillMaxSize(), viewModel: AuthViewModel
) {
    BackHandler(onBack = {
        viewModel.reducer.sendEvent(AuthEvent.DisplayLogin)
    }, enabled = true)

    SignUpScreenContent(modifier, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenContent(modifier: Modifier = Modifier.background(MaterialTheme.colorScheme.background), viewModel: AuthViewModel) {
    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val genderPickedIndex = remember { mutableStateOf(0) }
    val privacyAgreed = remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp)
    ) {

        Spacer(Modifier.height(32.dp))

        UniqloHeader()

        Spacer(Modifier.height(16.dp))

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Email", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

            AppOutlinedTextField(modifier = Modifier.fillMaxWidth().height(52.dp), placeholder = "email@gmail.com", onMessageSent = {
                email.value = it
            }, shape = RoundedCornerShape(10.dp), imeAction = ImeAction.Done, onValueChange = {
                email.value = it
            }, validator = {
                validateEmail(it)
            })

            Spacer(Modifier.height(22.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Password", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

            AppPasswordField(modifier = Modifier.fillMaxWidth().height(52.dp), placeholder = "password", onMessageSent = {
                password.value = it
            }, shape = RoundedCornerShape(10.dp), imeAction = ImeAction.Done, onValueChange = {
                password.value = it
            }, validator = {
                validatePassword(it)
            })

            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "Mật khẩu phải dài ít nhất 8 ký tự, bao gồm chữ cái và số, chỉ được sử dụng ký hiệu -_.@",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )

            Spacer(Modifier.height(22.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Họ tên", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

            AppOutlinedTextField(modifier = Modifier.fillMaxWidth().height(52.dp), placeholder = "Nhập họ tên", onMessageSent = {
                name.value = it
            }, shape = RoundedCornerShape(10.dp), imeAction = ImeAction.Done, onValueChange = {
                name.value = it
            }, validator = {
                validateNotEmpty(it)
            })

            Spacer(Modifier.height(22.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Ngày sinh", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

            Box(
                modifier = Modifier.fillMaxWidth().height(52.dp).border(
                    1.dp, MaterialTheme.colorScheme.onSurfaceVariant, RoundedCornerShape(10.dp)
                ).clickable {
                    // Handle click event
                    showDatePicker.value = true
                }, contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = datePickerState.selectedDateMillis?.millisToDateString(DD_MM_YYYY) ?: "dd/mm/yyyy",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp).wrapContentWidth()
                )

                Icon(imageVector = Calendar, contentDescription = null, modifier = Modifier.padding(end = 16.dp).size(24.dp, 24.dp).align(Alignment.CenterEnd))
            }

            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "Không thể thay đổi ngày sinh của bạn khi đăng ký",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )

            Spacer(Modifier.height(24.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Giới tính", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = genderPickedIndex.value == 0, onClick = {
                    genderPickedIndex.value = 0
                })
                Text(text = "Name", modifier = Modifier.padding(start = 8.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = genderPickedIndex.value == 1, onClick = {
                    genderPickedIndex.value = 1
                })
                Text(text = "Nữ", modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(Modifier.height(24.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Điều khoản người dùng", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "Bằng cách tạo tài khoản, bạn đồng ý với các điều khoản sử dụng và chính sách quyền riêng tư của UNIQLO",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )

            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.Top) {
                Checkbox(modifier = Modifier.fillMaxHeight(), checked = privacyAgreed.value, onCheckedChange = {
                    privacyAgreed.value = it
                })

                Text(
                    modifier = Modifier.padding(start = 16.dp).fillMaxWidth(), text = "Tôi đồng ý với ĐIỀU KHOẢN SỬ DỤNG và CHÍNH SÁCH QUYỀN RIÊNG TƯ của UNIQLO", maxLines = 2
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp).clickable {
                /* no-op */
                uriHandler.openUri("https://google.com")
            }, text = "ĐIỀU KHOẢN SỬ DỤNG", style = MaterialTheme.typography.bodyLarge, color = Color.Black, textDecoration = TextDecoration.Underline)

            Spacer(Modifier.height(24.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp).clickable {
                /* no-op */
                uriHandler.openUri("https://google.com")
            }, text = "CHÍNH SÁCH BẢO MẬT", style = MaterialTheme.typography.bodyLarge, color = Color.Black, textDecoration = TextDecoration.Underline)

            Spacer(Modifier.height(24.dp))

            BlackButtonIconEnd(modifier = Modifier.fillMaxWidth().height(52.dp), onClick = {
                val dob = datePickerState.selectedDateMillis?.millisToDateString(DD_MM_YYYY)
                val gender = if (genderPickedIndex.value == 0) "male" else "female"
                viewModel.signUp(email.value, password.value, name.value, dob ?: "", gender, privacyAgreed.value)
            }, text = {
                Text(text = "Tiếp tục", color = Color.White)
            })

            Spacer(Modifier.height(24.dp))

            if (showDatePicker.value) {
                AppDatePicker(onDismiss = {
                    showDatePicker.value = false
                }, onDateSelected = {
                    AppLogger.d("Selected date: $it")
                    showDatePicker.value = false
                }, showModeToggle = false, state = datePickerState
                )
            }
        }

        Spacer(Modifier.height(32.dp))
    }

}

