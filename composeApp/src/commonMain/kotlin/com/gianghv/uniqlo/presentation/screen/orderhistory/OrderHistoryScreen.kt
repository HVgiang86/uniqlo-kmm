package com.gianghv.uniqlo.presentation.screen.orderhistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderReducerHistoryScreen() {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Order History", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        })
    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Order History Screen", modifier = Modifier.align(Alignment.Center))
        }
    }
}
