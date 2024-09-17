package com.gianghv.uniqlo.presentation.screen.main

import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import org.jetbrains.compose.resources.DrawableResource
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_bottom_nav_home
import uniqlo.composeapp.generated.resources.ic_bottom_nav_more
import uniqlo.composeapp.generated.resources.ic_bottom_nav_profile
import uniqlo.composeapp.generated.resources.ic_bottom_nav_top5flights


enum class BottomNavItem(val iconRes: DrawableResource) {
    HOME(iconRes = Res.drawable.ic_bottom_nav_home),
    GITHUB(iconRes = Res.drawable.ic_bottom_nav_top5flights);


    fun asTopLevelDestination(): MainScreenDestination {
        return when (this) {
            HOME -> MainScreenDestination.Home
            GITHUB -> MainScreenDestination.GithubProfile
        }
    }
}

fun MainScreenDestination.asBottomNavItem(): BottomNavItem {
    return when (this) {
        MainScreenDestination.Home -> BottomNavItem.HOME

        else -> BottomNavItem.HOME
    }
}
