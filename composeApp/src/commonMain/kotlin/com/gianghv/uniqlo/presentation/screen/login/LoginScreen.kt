package com.gianghv.uniqlo.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.presentation.component.AppOutlinedTextField
import com.gianghv.uniqlo.presentation.component.AppPasswordField
import com.gianghv.uniqlo.presentation.component.BaseOutlinedButton
import com.gianghv.uniqlo.presentation.component.BlackButtonIconEnd
import com.gianghv.uniqlo.presentation.component.InputWrapper
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.component.MyAlertDialog
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.logging.AppLogger
import org.jetbrains.compose.resources.painterResource
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_uniqlo

@Composable
fun LoginScreen(modifier: Modifier = Modifier.fillMaxSize(), viewModel: LoginViewModel, onNavigateMain: () -> Unit) {

    val state by viewModel.state.asState()
    if (state.error?.shouldShowDialog == true) {
        MyAlertDialog(title = "Thông báo", content = state.error?.throwable?.message ?: "Unknown error!", rightBtnTitle = "OK", rightBtn = {})
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.loginSuccess) {
        // navigate to main
        onNavigateMain()
    }
    LoginScreenContent(modifier, viewModel)
}

@Composable
fun LoginScreenContent(modifier: Modifier = Modifier, viewModel: LoginViewModel) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Scaffold {
        Column(modifier = modifier.padding(32.dp)) {
            val isLoading = remember { mutableStateOf(false) }
            LoadingDialog(isLoading)

            Spacer(Modifier.height(32.dp))

            UniqloHeader()

            Spacer(Modifier.weight(1f))

            Column {
                Text(modifier = Modifier.padding(bottom = 8.dp), text = "Email", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

                AppOutlinedTextField(modifier = Modifier.fillMaxWidth().height(52.dp),
                    input = InputWrapper(),
                    placeholder = "email@gmail.com",
                    onMessageSent = {
                        email.value = it
                    },
                    shape = RoundedCornerShape(10.dp),
                    imeAction = ImeAction.Done,
                    onValueChange = {
                        email.value = it
                    })

                Spacer(Modifier.height(22.dp))

                Text(modifier = Modifier.padding(bottom = 8.dp), text = "Password", style = MaterialTheme.typography.bodyLarge, color = Color.Black)

                val passwordInputWrapper = InputWrapper()
                AppPasswordField(modifier = Modifier.fillMaxWidth().height(52.dp), input = passwordInputWrapper, placeholder = "password", onMessageSent = {
                    password.value = it
                }, shape = RoundedCornerShape(10.dp), imeAction = ImeAction.Done, onValueChange = {
                    password.value = it
                    passwordInputWrapper.validate {
                        validatePassword(it)
                    }
                })

                Spacer(Modifier.height(32.dp))

                BlackButtonIconEnd(modifier = Modifier.fillMaxWidth().height(52.dp), onClick = {
                    // do login
                    viewModel.login(email.value, password.value)
                }, text = {
                    Text(text = "Continue", color = Color.White)
                })

                Spacer(Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth().height(1.dp))
                    Box(modifier = Modifier.wrapContentWidth().background(Color.White)) {
                        Text(text = "or", color = Color.Black, modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                    }
                }

                Spacer(Modifier.height(24.dp))

                BaseOutlinedButton(onClick = {
                    // do sign up navigate
                }, text = {
                    Text(text = "Sign up", color = Color.Black)
                }, modifier = Modifier.fillMaxWidth().height(52.dp), enable = true
                )
            }

            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
fun UniqloHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(Res.drawable.ic_uniqlo), contentDescription = null, modifier = Modifier.size(44.dp)
        )

        Spacer(Modifier.width(16.dp))

        Text(text = "UNIQLO", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
    }
}

