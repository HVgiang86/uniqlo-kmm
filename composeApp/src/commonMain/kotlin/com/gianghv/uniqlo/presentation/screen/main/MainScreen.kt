package com.gianghv.uniqlo.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.gianghv.uniqlo.presentation.component.MyAppToolbar
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenNavigation
import com.gianghv.uniqlo.presentation.screen.main.navigation.isTopLevelScreen
import com.gianghv.uniqlo.theme.Silver_d8
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.logging.AppLogger
import org.jetbrains.compose.resources.painterResource


@Composable
fun MainScreen(viewModel: MainViewModel) {
    MainScreenNavigation(viewModel)
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: MainViewModel,
    currentDestination: MainScreenDestination,
    onDestinationChanged: (MainScreenDestination) -> Unit,
    onNavigateBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    val state by viewModel.state.asState()
    state.data

    Scaffold(topBar = {
        val isToolbarInvisible = (currentDestination == MainScreenDestination.Home)

        AnimatedVisibility(isToolbarInvisible.not()) {
            MyAppToolbar(title = if (isToolbarInvisible) "" else currentDestination.getTitle(), onNavigationIconClick = { onNavigateBack() })
        }
    }, content = {
        content()
    }, bottomBar = {
        if (currentDestination.isTopLevelScreen()) {
            BottomNavigation(selectedNavItem = currentDestination.asBottomNavItem(), onNavigationItemSelected = {
                onDestinationChanged(it.asTopLevelDestination())
            })
        }
    })
}

@Composable
private fun BottomNavigation(
    modifier: Modifier = Modifier,
    selectedNavItem: BottomNavItem,
    onNavigationItemSelected: (BottomNavItem) -> Unit,
) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background, modifier = modifier
    ) {
        val navItems = BottomNavItem.values()
        navItems.forEach { item ->
            val isSelected = item == selectedNavItem
            val iconTint = if (isSelected) MaterialTheme.colorScheme.secondary else Silver_d8
            NavigationBarItem(icon = {
                Icon(
                    painter = painterResource(item.iconRes), contentDescription = null, tint = iconTint
                )
            }, selected = isSelected, alwaysShowLabel = false,

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.background,
                    unselectedIconColor = Silver_d8,
                ), onClick = { onNavigationItemSelected(item) })
        }
    }
}
