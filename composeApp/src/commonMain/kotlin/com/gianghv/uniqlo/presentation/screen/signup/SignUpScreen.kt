package com.gianghv.uniqlo.presentation.screen.signup

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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.constant.DD_MM_YYYY
import com.gianghv.uniqlo.presentation.component.AppDatePicker
import com.gianghv.uniqlo.presentation.component.AppOutlinedTextField
import com.gianghv.uniqlo.presentation.component.AppPasswordField
import com.gianghv.uniqlo.presentation.component.BlackButtonIconEnd
import com.gianghv.uniqlo.presentation.component.InputWrapper
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.component.MyAlertDialog
import com.gianghv.uniqlo.presentation.screen.login.LoginViewModel
import com.gianghv.uniqlo.presentation.screen.login.UniqloHeader
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.theme.icons.Calendar
import com.gianghv.uniqlo.util.ext.millisToDateString
import com.gianghv.uniqlo.util.logging.AppLogger

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: LoginViewModel,
    currentDestination: MainScreenDestination,
    onDestinationChanged: (MainScreenDestination) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val state = viewModel.state.collectAsState()
    if (state.value.error?.shouldShowDialog == true) {
        MyAlertDialog(title = "Thông báo", content = state.value.error?.throwable?.message ?: "Unknown error!", rightBtnTitle = "OK", rightBtn = {
            viewModel.onClearErrorState()
        })

        SignUpScreenContent(modifier, viewModel)
    }
}


@Composable
fun SignUpScreenContent(modifier: Modifier = Modifier, viewModel: LoginViewModel) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenContent(modifier: Modifier = Modifier) {
    val isLoading = remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val genderPickedIndex = remember { mutableStateOf(0) }
    val privacyAgreed = remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 16.dp)
    ) {

        LoadingDialog(isLoading)

        Spacer(Modifier.height(32.dp))

        UniqloHeader()

        Spacer(Modifier.height(16.dp))

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Email", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

            AppOutlinedTextField(modifier = Modifier.fillMaxWidth().height(52.dp), input = InputWrapper(), placeholder = "email@gmail.com", onMessageSent = {
                AppLogger.d("Text: $it")
            }, shape = RoundedCornerShape(10.dp), imeAction = ImeAction.Done, onValueChange = {

            })

            Spacer(Modifier.height(22.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Password", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

            val passwordInputWrapper = InputWrapper()
            AppPasswordField(modifier = Modifier.fillMaxWidth().height(52.dp), input = passwordInputWrapper, placeholder = "password", onMessageSent = {
                AppLogger.d("Text: $it")
            }, shape = RoundedCornerShape(10.dp), imeAction = ImeAction.Done, onValueChange = {
                passwordInputWrapper.validate {
                    validatePassword(it)
                }
            })

            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "The password must be at least 8 characters long, and consists of letters and numbers, only the -_.@ symbol can be used",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )

            Spacer(Modifier.height(22.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Your name", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

            AppOutlinedTextField(modifier = Modifier.fillMaxWidth().height(52.dp), input = InputWrapper(), placeholder = "Enter your name", onMessageSent = {
                AppLogger.d("Text: $it")
            }, shape = RoundedCornerShape(10.dp), imeAction = ImeAction.Done, onValueChange = {

            })

            Spacer(Modifier.height(22.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Date of birth", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

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
                text = "Cannot change your date of birth once you have registered",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )

            Spacer(Modifier.height(24.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Gender", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = genderPickedIndex.value == 0, onClick = {
                    genderPickedIndex.value = 0
                })
                Text(text = "Male", modifier = Modifier.padding(start = 8.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = genderPickedIndex.value == 1, onClick = {
                    genderPickedIndex.value = 1
                })
                Text(text = "Female", modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(Modifier.height(24.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp), text = "Membership Agreement", style = MaterialTheme.typography.bodyLarge, color = Color.Black)
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "By creating an account, you agree to UNIQLO's terms of use and privacy policy",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )

            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.Top) {
                Checkbox(modifier = Modifier.fillMaxHeight(), checked = privacyAgreed.value, onCheckedChange = {
                    privacyAgreed.value = it
                })

                Text(
                    modifier = Modifier.padding(start = 16.dp).fillMaxWidth(), text = "I agree to the UNIQLO TERMS OF USE and PRIVACY POLICY", maxLines = 2
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp).clickable {
                /* no-op */
                uriHandler.openUri("https://google.com")
            }, text = "TERMS OF USE", style = MaterialTheme.typography.bodyLarge, color = Color.Black, textDecoration = TextDecoration.Underline)

            Spacer(Modifier.height(24.dp))

            Text(modifier = Modifier.padding(bottom = 8.dp).clickable {
                /* no-op */
                uriHandler.openUri("https://google.com")
            }, text = "PRIVACY POLICY", style = MaterialTheme.typography.bodyLarge, color = Color.Black, textDecoration = TextDecoration.Underline)

            Spacer(Modifier.height(24.dp))

            BlackButtonIconEnd(modifier = Modifier.fillMaxWidth().height(52.dp), onClick = {
                // do login
                showDatePicker.value = true
            }, text = {
                Text(text = "Continue", color = Color.White)
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


@Composable
fun PreviewSignUpScreen() {
    SignUpScreenContent()
}
