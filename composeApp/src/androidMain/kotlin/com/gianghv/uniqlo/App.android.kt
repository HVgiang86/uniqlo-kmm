package com.gianghv.uniqlo

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.presentation.component.AppOutlinedTextField
import com.gianghv.uniqlo.presentation.component.InputWrapper
import com.gianghv.uniqlo.presentation.screen.signup.PreviewSignUpScreen
import com.gianghv.uniqlo.rootview.App
import com.gianghv.uniqlo.util.logging.AppLogger

@Composable
fun MainView() {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.run {
//                val systemBarColor = Color(0xFFFFD700).toArgb()
//                statusBarColor = systemBarColor
//                WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
            }
        }
    }

    App()
}

@Preview
@Composable
fun AppPreview() {
//    MyCircularProgressIndicator()
//    MyAlertDialog(title = "Notification", content = "This is the dialog content")
//    UserInputField(onMessageSent = {})
    PreviewSignUpScreen()
}
