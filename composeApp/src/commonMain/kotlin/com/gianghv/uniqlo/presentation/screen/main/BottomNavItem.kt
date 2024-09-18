package com.gianghv.uniqlo.presentation.screen.main

import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import org.jetbrains.compose.resources.DrawableResource
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_bottom_nav_ai_chat
import uniqlo.composeapp.generated.resources.ic_bottom_nav_home
import uniqlo.composeapp.generated.resources.ic_bottom_nav_profile
import uniqlo.composeapp.generated.resources.ic_bottom_nav_wishlist


enum class BottomNavItem(val iconRes: DrawableResource) {
    HOME(iconRes = Res.drawable.ic_bottom_nav_home),
    WISH_LIST(iconRes = Res.drawable.ic_bottom_nav_wishlist),
    AI_CHAT(iconRes = Res.drawable.ic_bottom_nav_ai_chat),
    PROFILE(iconRes = Res.drawable.ic_bottom_nav_profile);

    fun asTopLevelDestination(): MainScreenDestination {
        return when (this) {
            HOME -> MainScreenDestination.Home
            WISH_LIST -> MainScreenDestination.WishList
            AI_CHAT -> MainScreenDestination.AiChat
            PROFILE -> MainScreenDestination.Profile
        }
    }
}

fun MainScreenDestination.asBottomNavItem(): BottomNavItem {
    return when (this) {
        MainScreenDestination.Home -> BottomNavItem.HOME
        MainScreenDestination.WishList -> BottomNavItem.WISH_LIST
        MainScreenDestination.AiChat -> BottomNavItem.AI_CHAT
        MainScreenDestination.Profile -> BottomNavItem.PROFILE
        else -> BottomNavItem.HOME
    }
}
