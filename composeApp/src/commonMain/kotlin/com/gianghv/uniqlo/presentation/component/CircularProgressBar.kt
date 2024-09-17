package com.gianghv.uniqlo.presentation.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gianghv.uniqlo.theme.md_theme_light_secondary
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MyCircularProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = md_theme_light_secondary
    )
}
