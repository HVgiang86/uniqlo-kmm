package com.gianghv.uniqlo.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
actual fun getScreenHeightInDp(): Float {
    val configuration = LocalConfiguration.current
    val screenHeightPx = configuration.screenHeightDp
    return screenHeightPx.dp.value
}

@Composable
actual fun getScreenWidthInDp(): Float {
    val configuration = LocalConfiguration.current
    val screenWidthPx = configuration.screenWidthDp
    return screenWidthPx.dp.value
}
