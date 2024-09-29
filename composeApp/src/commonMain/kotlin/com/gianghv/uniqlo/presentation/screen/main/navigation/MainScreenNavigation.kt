package com.gianghv.uniqlo.presentation.screen.main.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gianghv.uniqlo.base.getViewModel
import com.gianghv.uniqlo.presentation.screen.aichat.AIChatScreen
import com.gianghv.uniqlo.presentation.screen.home.HomeScreen
import com.gianghv.uniqlo.presentation.screen.home.HomeViewModel
import com.gianghv.uniqlo.presentation.screen.main.MainScreen
import com.gianghv.uniqlo.presentation.screen.main.MainViewModel
import com.gianghv.uniqlo.presentation.screen.profile.ProfileScreen
import com.gianghv.uniqlo.presentation.screen.wishlist.WishListScreen
import com.gianghv.uniqlo.util.logging.AppLogger
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ai_chat
import uniqlo.composeapp.generated.resources.home
import uniqlo.composeapp.generated.resources.profile
import uniqlo.composeapp.generated.resources.wishlist

interface TopLevelScreenDestination : MainScreenDestination {
    companion object {
        fun getStartScreen(): TopLevelScreenDestination = MainScreenDestination.Home
    }
}

interface MainScreenDestination {
    object Home : Screen, TopLevelScreenDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val viewModel: HomeViewModel = getViewModel()
            AppLogger.d("start Home Screen")
            HomeScreen(viewModel)
        }

        override fun getTitle() = Res.string.home.toString()
    }

    object WishList : Screen, TopLevelScreenDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            WishListScreen()
        }

        override fun getTitle() = Res.string.wishlist.toString()

    }

    object AiChat : Screen, TopLevelScreenDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            AIChatScreen()
        }

        override fun getTitle() = Res.string.ai_chat.toString()
    }

    object Profile : Screen, TopLevelScreenDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            ProfileScreen()
        }

        override fun getTitle() = Res.string.profile.toString()
    }

    fun getTitle(): String {
        return ""
    }
}

fun MainScreenDestination.isTopLevelScreen() = this is TopLevelScreenDestination

fun Navigator.navigate(destination: MainScreenDestination) {
    val destinationScreen = destination as Screen
    when {
        destination.isTopLevelScreen() && destination == TopLevelScreenDestination.getStartScreen() -> this.replaceAll(destinationScreen)

        destination.isTopLevelScreen() -> with(this) {
            popUntilRoot()
            push(destinationScreen)
        }

        else -> this.push(destinationScreen)
    }
}

@Composable
fun MainScreenNavigation(viewModel: MainViewModel) {
    val startScreen = TopLevelScreenDestination.getStartScreen() as Screen
    Navigator(screen = startScreen) { navigator ->
        val currentScreen = navigator.lastItem
        val currentDestination = currentScreen as MainScreenDestination
        MainScreen(viewModel = viewModel, currentDestination = currentDestination, onDestinationChanged = {
            navigator.navigate(it)
        }, onNavigateBack = {
            navigator.pop()

        }) {
//            CurrentScreen()
            AnimatedTransition(navigator)
        }
    }
}

@Composable
private fun AnimatedTransition(navigator: Navigator) {
    AnimatedContent(targetState = navigator.lastItem, transitionSpec = {

        val (initialScale, targetScale) = when (navigator.lastEvent) {
            StackEvent.Pop -> 1f to 0.85f
            else -> 0.85f to 1f
        }

        val stiffness = Spring.StiffnessMediumLow
        val enterTransition = fadeIn(tween(easing = EaseIn)) + scaleIn(
            spring(stiffness = stiffness), initialScale = initialScale
        )

        val exitTransition = fadeOut(spring(stiffness = stiffness)) + scaleOut(
            tween(easing = EaseOut), targetScale = targetScale
        )

        enterTransition togetherWith exitTransition
    }) { currentScreen ->
        navigator.saveableState("transition", currentScreen) {
            currentScreen.Content()
        }
    }
}
