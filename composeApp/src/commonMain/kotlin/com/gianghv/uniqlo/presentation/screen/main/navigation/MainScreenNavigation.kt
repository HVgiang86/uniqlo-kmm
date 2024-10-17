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
import com.gianghv.uniqlo.presentation.screen.aichat.AiChatViewModel
import com.gianghv.uniqlo.presentation.screen.cart.CartScreen
import com.gianghv.uniqlo.presentation.screen.cart.CartViewModel
import com.gianghv.uniqlo.presentation.screen.home.HomeScreen
import com.gianghv.uniqlo.presentation.screen.home.HomeViewModel
import com.gianghv.uniqlo.presentation.screen.main.MainScreen
import com.gianghv.uniqlo.presentation.screen.main.MainViewModel
import com.gianghv.uniqlo.presentation.screen.productdetail.ProductDetailScreen
import com.gianghv.uniqlo.presentation.screen.productdetail.ProductDetailViewModel
import com.gianghv.uniqlo.presentation.screen.profile.ProfileScreen
import com.gianghv.uniqlo.presentation.screen.profile.ProfileViewModel
import com.gianghv.uniqlo.presentation.screen.searchresult.SearchResultScreen
import com.gianghv.uniqlo.presentation.screen.searchresult.SearchResultScreenType
import com.gianghv.uniqlo.presentation.screen.searchresult.SearchResultViewModel
import com.gianghv.uniqlo.presentation.screen.wishlist.WishListScreen
import com.gianghv.uniqlo.presentation.screen.wishlist.WishListViewModel
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

abstract class WithParam(open val params: Map<String, Any>) : MainScreenDestination {
    inline fun <reified T> getValue(key: String): T? {
        return params[key] as? T
    }

    inline fun <reified T> getValue(key: String, default: T): T {
        val temp = params[key] as? T
        return temp ?: default
    }
}

abstract class LogoutFromDestination(open var onLogout: (() -> Unit)? = null) : MainScreenDestination

interface MainScreenDestination {
    class ProductDetail(params: Map<String, Any>) : Screen, WithParam(params) {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val productId: Long? = getValue(PRODUCT_ID_KEY)
            val viewModel: ProductDetailViewModel = getViewModel()
            ProductDetailScreen(viewModel, onBack = {
                navigator.pop()
            }, productId = productId, navigateTo = {
                navigator.navigate(it)
            })

        }

        companion object {
            const val PRODUCT_ID_KEY = "product_id_key"
        }
    }

    class SearchResult(params: Map<String, Any>) : Screen, WithParam(params) {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val searchQuery: String? = getValue(PRODUCT_SEARCH_QUERY_KEY)
            val screenType: SearchResultScreenType = getValue(SEARCH_RESULT_SCREEN_TYPE, SearchResultScreenType.ALL)
            val viewModel: SearchResultViewModel = getViewModel()
            SearchResultScreen(viewModel, onBack = {
                navigator.pop()
            }, screenType = screenType, searchQuery = searchQuery, navigateTo = {
                navigator.navigate(it)
            })

        }

        companion object {
            const val PRODUCT_SEARCH_QUERY_KEY = "product_search_query"
            const val SEARCH_RESULT_SCREEN_TYPE = "search_result_screen_type"
        }
    }

    object Cart : Screen, MainScreenDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val viewModel: CartViewModel = getViewModel()
            CartScreen(viewModel, onBack = {
                navigator.pop()
            })
        }
    }

    object Home : Screen, TopLevelScreenDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val viewModel: HomeViewModel = getViewModel()
            HomeScreen(viewModel, navigateTo = {
                navigator.navigate(it)
            })
        }

        override fun getTitle() = Res.string.home.toString()
    }

    object WishList : Screen, TopLevelScreenDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val viewModel: WishListViewModel = getViewModel()
            WishListScreen(viewModel, navigateTo = {
                navigator.navigate(it)
            })
        }

        override fun getTitle() = Res.string.wishlist.toString()
    }

    object AiChat : Screen, TopLevelScreenDestination {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val viewModel: AiChatViewModel = getViewModel()
            AIChatScreen(viewModel, navigateTo = {
                navigator.navigate(it)
            })
        }

        override fun getTitle() = Res.string.ai_chat.toString()
    }

    class Profile : Screen, TopLevelScreenDestination, LogoutFromDestination() {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val viewModel: ProfileViewModel = getViewModel()
            ProfileScreen(viewModel, navigateTo = {
                navigator.navigate(it)
            }, onLogout = {
                onLogout?.let { it() }
            })
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
fun MainScreenNavigation(viewModel: MainViewModel, onLogout: () -> Unit) {
    val startScreen = TopLevelScreenDestination.getStartScreen() as Screen
    Navigator(screen = startScreen) { navigator ->
        val currentScreen = navigator.lastItem
        val currentDestination = currentScreen as MainScreenDestination
        MainScreen(viewModel = viewModel, currentDestination = currentDestination, onDestinationChanged = {
            navigator.navigate(it)
        }, onNavigateBack = {
            navigator.pop()
        }, onLogout = {
            onLogout()
        }) {
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
