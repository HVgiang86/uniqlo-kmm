package com.gianghv.uniqlo.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun WebView(modifier: Modifier = Modifier, url: String)
