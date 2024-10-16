package com.gianghv.uniqlo.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.presentation.screen.main.navigation.LogoutFromDestination
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenNavigation
import com.gianghv.uniqlo.presentation.screen.main.navigation.isTopLevelScreen
import com.gianghv.uniqlo.theme.Black
import com.gianghv.uniqlo.theme.Divider_color
import org.jetbrains.compose.resources.painterResource


@Composable
fun MainScreen(viewModel: MainViewModel, onLogout: () -> Unit) {
    MainScreenNavigation(viewModel, onLogout)
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: MainViewModel,
    currentDestination: MainScreenDestination,
    onDestinationChanged: (MainScreenDestination) -> Unit,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(content = {
        content()
    }, bottomBar = {
        if (currentDestination.isTopLevelScreen()) {
            BottomNavigation(selectedNavItem = currentDestination.asBottomNavItem(), onNavigationItemSelected = {
                onDestinationChanged(it.asTopLevelDestination())
            })

            if (currentDestination is LogoutFromDestination) {
                currentDestination.onLogout = onLogout
            }
        }
    })
}

@Composable
private fun BottomNavigation(
    modifier: Modifier = Modifier,
    selectedNavItem: BottomNavItem,
    onNavigationItemSelected: (BottomNavItem) -> Unit,
) {
    Column(modifier = Modifier.wrapContentHeight()) {
        HorizontalDivider(thickness = 1.dp, color = Divider_color)
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background, modifier = modifier.height(80.dp)
        ) {
            val navItems = BottomNavItem.entries.toTypedArray()
            navItems.forEach { item ->
                val isSelected = item == selectedNavItem
                val iconTint = if (isSelected) MaterialTheme.colorScheme.secondary else Black
                NavigationBarItem(modifier = Modifier.size(48.dp).align(alignment = Alignment.CenterVertically), icon = {
                    Icon(
                        painter = painterResource(item.iconRes), contentDescription = null, tint = iconTint
                    )
                }, selected = isSelected, alwaysShowLabel = false,

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        indicatorColor = MaterialTheme.colorScheme.background,
                        unselectedIconColor = Black,
                    ), onClick = { onNavigationItemSelected(item) })
            }
        }
    }
}
