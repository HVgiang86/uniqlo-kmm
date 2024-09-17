package com.gianghv.uniqlo.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gianghv.uniqlo.theme.icons.ArrowRight

@Composable
fun RedFilledTextButton(onClick: () -> Unit, modifier: Modifier = Modifier, text: @Composable () -> Unit) {
    FilledTextButton(
        onClick, modifier, colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onTertiary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        text()
    }
}


@Composable
fun BlackFilledTextButton(onClick: () -> Unit, modifier: Modifier = Modifier, text: @Composable () -> Unit) {
    FilledTextButton(
        onClick, modifier, colors = ButtonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
            disabledContentColor = MaterialTheme.colorScheme.onTertiary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        text()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlackButtonIconEnd(onClick: () -> Unit, modifier: Modifier = Modifier, text: @Composable () -> Unit) {

    FilledTextButton(
        onClick, modifier = modifier.fillMaxWidth(), colors = ButtonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
            disabledContentColor = MaterialTheme.colorScheme.onTertiary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            text()
            Icon(
                imageVector = ArrowRight, contentDescription = null, modifier = Modifier.width(24.dp).align(Alignment.CenterEnd)
            )
        }

    }
}

@Composable
fun FilledTextButton(
    onClick: () -> Unit, text: String, modifier: Modifier = Modifier, enable: Boolean = true, colors: ButtonColors = ButtonDefaults.textButtonColors()
) {
    Button(
        onClick = {
            onClick.invoke()
        }, shape = RoundedCornerShape(8.dp), modifier = modifier, colors = colors, enabled = enable
    ) {
        Text(
            text = text, textAlign = TextAlign.Center, fontSize = 24.sp, style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun BaseOutlinedButton(
    onClick: () -> Unit, text: @Composable () -> Unit, modifier: Modifier = Modifier, enable: Boolean = true, colors: ButtonColors = ButtonDefaults.textButtonColors()
) {
    OutlinedButton(onClick = {
        onClick.invoke()
    }, shape = RoundedCornerShape(8.dp), modifier = modifier, colors = colors, enabled = enable, content = {
        text()
    })
}

@Composable
fun FilledTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    text: @Composable () -> Unit
) {
    Button(
        onClick = {
            onClick.invoke()
        }, shape = RoundedCornerShape(8.dp), modifier = modifier, colors = colors, enabled = enable
    ) {
        text()
    }
}
