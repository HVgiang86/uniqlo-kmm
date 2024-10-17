package com.gianghv.uniqlo.presentation.screen.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(onBack: () -> Unit) {
    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), title = {
            Text(text = "Order", style = MaterialTheme.typography.titleMedium, color = Color.Black)
        }, actions = {
            IconButton(onClick = {

            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) {
        LazyColumn  () {

        }

    }
}
