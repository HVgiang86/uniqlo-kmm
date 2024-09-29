package com.gianghv.uniqlo.presentation.screen.aichat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AIChatScreen() {
    val openDialog = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(0) { 5 }
    val tabItems = listOf(
        TabItem("Actions", Icons.Filled.Menu, Icons.Outlined.Menu),
        TabItem("Input", Icons.Filled.Search, Icons.Outlined.Search),
        TabItem("Navigation", Icons.Filled.AddCircle, Icons.Outlined.AddCircle),
        TabItem("Color", Icons.Filled.AddCircle, Icons.Outlined.AddCircle),
        TabItem("Typo", Icons.Filled.AddCircle, Icons.Outlined.AddCircle)
    )

    val scrollState = rememberScrollState()
    Column {
        TabRow(selectedTabIndex = pagerState.currentPage, divider = {
            HorizontalDivider(thickness = 3.dp)
        }, indicator = { position ->
            SecondaryIndicator(
                Modifier.tabIndicatorOffset(position[pagerState.currentPage]), color = MaterialTheme.colorScheme.primary
            )
        }, tabs = {
            tabItems.forEachIndexed { index, tabItem ->
                LeadingIconTab(selected = pagerState.currentPage == index, onClick = {
                    scope.launch {
                        pagerState.requestScrollToPage(index)
                    }
                }, icon = {
                    val vector = if (pagerState.currentPage == index) tabItem.selectedIcon else tabItem.unselectedIcon
                    Icon(imageVector = vector, contentDescription = null)
                }, text = {
                    Text(text = tabItem.title)
                })
            }
        }, modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = 60.dp))

        when (pagerState.currentPage) {
            0 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Actions()
                }
            }

            1 -> {
                Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
                    TextInputs()
                }
            }

            2 -> {
                Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
                    Navigation()
                }
            }

            3 -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ColorScreen()
                }
            }

            4 -> {
                TypographyScreen()
            }
        }
    }
}


