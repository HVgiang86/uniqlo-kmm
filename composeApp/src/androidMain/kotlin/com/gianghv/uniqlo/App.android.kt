package com.gianghv.uniqlo

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.gianghv.uniqlo.rootview.App

@Composable
fun MainView() {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.run {
                val systemBarColor = Color(0xFFFFFFFF).toArgb()
                statusBarColor = systemBarColor
                WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
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
}
