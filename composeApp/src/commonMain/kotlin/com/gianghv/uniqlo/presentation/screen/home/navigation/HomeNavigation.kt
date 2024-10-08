@file:OptIn(InternalVoyagerApi::class)

package com.gianghv.uniqlo.presentation.screen.home.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.gianghv.uniqlo.base.getViewModel
import com.gianghv.uniqlo.presentation.screen.productdetail.ProductDetailScreen
import com.gianghv.uniqlo.presentation.screen.productdetail.ProductDetailViewModel

abstract class WithParam(open val params: Map<String, Any>) : HomeDestination {
    inline fun <reified T> getValue(key: String): T? {
        return params[key] as? T
    }

    inline fun <reified T> getValue(key: String, default: T): T {
        val temp = params[key] as? T
        return temp ?: default
    }
}

interface HomeDestination {
    class ProductDetail(params: Map<String, Any>) : Screen, WithParam(params) {
        @Composable
        override fun Content() {
            val navigator = LocalNavigator.currentOrThrow
            val productId: Long? = getValue(PRODUCT_ID_KEY)
            val viewModel: ProductDetailViewModel = getViewModel()
            ProductDetailScreen(viewModel, onBack = {
                navigator.pop()
            }, productId = productId)

            BackHandler(enabled = true){
                navigator.pop()
            }

        }

        companion object {
            const val PRODUCT_ID_KEY = "product_id_key"
        }
    }
}

@Composable
fun HomeNavigation(destination: HomeDestination) {
    Navigator(screen = destination as Screen)
}
