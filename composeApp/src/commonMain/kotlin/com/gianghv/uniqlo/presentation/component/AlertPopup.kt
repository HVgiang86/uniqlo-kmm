package com.gianghv.uniqlo.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.util.logging.AppLogger


@Composable
fun MyAlertDialog(
    title: String,
    content: String,
    leftBtnTitle: String = "Dismiss",
    rightBtnTitle: String = "Ok",
    leftBtn: (() -> Unit)? = null,
    rightBtn: (() -> Unit)? = null,
    state: MutableState<Boolean>? = null,
    cancelable: Boolean = true
) {
    AppLogger.d("MyAlertDialog: $title, $content, $leftBtnTitle, $rightBtnTitle, $leftBtn, $rightBtn, $state, $cancelable")
    val openDialog = state ?: remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(onDismissRequest = {
            if (cancelable) openDialog.value = false
        }, title = {
            Text(
                text = title, modifier = Modifier.padding(top = 8.dp)
            )
        }, text = {
            Text(
                text = content, modifier = Modifier.padding(8.dp), maxLines = 8
            )
        }, confirmButton = {
            if (rightBtn != null) {
                Button(onClick = {
                    // Handle confirm button click here
                    rightBtn.invoke()
                    openDialog.value = false
                }, shape = RoundedCornerShape(8.dp)) {
                    Text(rightBtnTitle)
                }
            } else {
                Button(onClick = {
                    // Handle confirm button click here
                    openDialog.value = false
                }, shape = RoundedCornerShape(8.dp)) {
                    Text("OK")
                }
            }
        }, dismissButton = {
            if (leftBtn != null) {
                TextButton(onClick = {
                    // Handle dismiss button click here
                    leftBtn.invoke()
                    openDialog.value = false
                }) {
                    Text(leftBtnTitle)
                }
            }
        }, shape = RoundedCornerShape(8.dp))
    }
}
