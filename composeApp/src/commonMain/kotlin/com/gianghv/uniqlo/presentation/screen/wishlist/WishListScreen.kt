package com.gianghv.uniqlo.presentation.screen.wishlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun WishListScreen() {
    val openDialog = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("WishList", style = MaterialTheme.typography.bodyLarge)
    }
}
