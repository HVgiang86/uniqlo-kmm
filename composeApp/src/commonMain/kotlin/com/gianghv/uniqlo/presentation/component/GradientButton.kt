package com.gianghv.uniqlo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.theme.Orange
import com.gianghv.uniqlo.theme.Orange_51


@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    text: String = "",
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)

    if (isLoading) {
        Box(modifier = modifier.height(64.dp), contentAlignment = Alignment.Center) {
            MyCircularProgressIndicator()
        }
    }

    if (isLoading.not()) {
        Button(
            modifier = modifier.height(64.dp).clip(shape).background(
                    brush = Brush.linearGradient(
                        0f to Orange, 1f to Orange_51
                    )
                ),
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = { onClick() },
        ) {
            Text(
                text = text, color = Color.White, style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
